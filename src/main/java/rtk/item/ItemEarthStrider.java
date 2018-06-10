package rtk.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.inv.BaublesInventoryWrapper;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import rtk.ModBlocks;
import rtk.ModConfig;
import rtk.ModItems;
import rtk.block.BlockHole;
import rtk.common.CMath;
import rtk.common.CNBT;
import rtk.common.Common;
import rtk.tileentity.TileHole;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class ItemEarthStrider extends ItemBase implements IBauble {
    public ItemEarthStrider(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
        setMaxDamage(10000);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            return EnumActionResult.FAIL;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != Blocks.BEDROCK)
            return EnumActionResult.FAIL;
        BlockPos cornerA = new BlockPos(pos.getX() - 1, 0, pos.getZ() - 1);
        BlockPos cornerB = new BlockPos(pos.getX() + 1, 5, pos.getZ() + 1);
        for (BlockPos p: CMath.cuboid(cornerA, cornerB))
            world.setBlockToAir(p);
        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 0.5F, false);
        ItemStack stack = player.getHeldItem(hand);
        player.setHeldItem(hand, ItemStack.EMPTY);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        nbt.setBoolean("active", !nbt.getBoolean("active"));

        if (!world.isRemote)
            player.sendMessage(new TextComponentTranslation(nbt.getBoolean("active")? "item.earthstrider.active" : "item.earthstrider.inactive"));

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return CNBT.ensureCompound(stack).getBoolean("active");
    }

    public boolean isWhitelisted(Block block) {
        for (String registryName: ModConfig.earthStriderWhitelist)
            if (registryName.equals(block.getRegistryName().toString()))
                return true;
        return false;
    }

    public void doUpdate(ItemStack stack, EntityPlayer player, IInventory inventory, int itemSlot, boolean active) {

        NBTTagCompound nbt = CNBT.ensureCompound(stack);

        if (active && nbt.getBoolean("working")) {
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40));
            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 40));
        }

        World world = player.world;
        if (world.isRemote || !active)
            return;

        Vec3d center = Common.getTrueCenter(player);
        BlockPos foot = new BlockPos(center.x, player.posY + 0.5, center.z);

        if (player.isSneaking()){
            if (!nbt.getBoolean("alreadySneaking"))
                foot = foot.add(0, -1 ,0);
            nbt.setBoolean("alreadySneaking", true);
        } else {
            nbt.setBoolean("alreadySneaking", false);
        }

        int holeVolume = 0;

        int edgeDist = 7;
        for (BlockPos pos : CMath.cuboid(foot.add(-edgeDist, 0, -edgeDist), foot.add(edgeDist, 3, edgeDist))){
            int dx = pos.getX() - foot.getX();
            int dy = pos.getY() - foot.getY();
            int dz = pos.getZ() - foot.getZ();
            dx = Math.abs(dx);
            dz = Math.abs(dz);

            boolean isEdge = dx == edgeDist || dy == 3 || dz == edgeDist;

            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockHole){
                TileHole hole = (TileHole) world.getTileEntity(pos);
                if (!isEdge){
                    hole.setTimeLeft(5);
                    holeVolume++;
                } else {
                    hole.replace(true);
                }
            }

            boolean whitelisted = isWhitelisted(state.getBlock());

            if (world.isRemote || !whitelisted || isEdge || !canMakeHole(world, pos))
                continue;

            world.setBlockState(pos, ModBlocks.hole.getDefaultState());
            TileHole hole = (TileHole) world.getTileEntity(pos);
            hole.setPrevState(state);
        }

        if (holeVolume >= 200 && !player.capabilities.isCreativeMode){
            nbt.setBoolean("working", true);
            stack.setItemDamage(stack.getItemDamage() + 1);
            if (stack.getItemDamage() >= stack.getMaxDamage()) {
                inventory.setInventorySlotContents(itemSlot, new ItemStack(ModItems.earthStriderDrained));
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
            }
        } else {
            nbt.setBoolean("working", false);
        }
    }

    public boolean canMakeHole(World world, BlockPos pos){
        for (EnumFacing dir : EnumFacing.VALUES){
            BlockPos n = pos.offset(dir);
            IBlockState state = world.getBlockState(n);
            if (state.getBlock() instanceof BlockLiquid || getSupportDirection(state) == dir)
                return false;
        }

        return true;
    }

    public boolean supportsUp(IBlockState state){
        Block block = state.getBlock();

        if (block == Blocks.GRAVEL || block == Blocks.SAND)
            return true;
        if (block instanceof BlockDoor || block instanceof BlockRailBase)
            return true;
        if (block instanceof BlockRedstoneDiode || block instanceof BlockRedstoneWire)
            return true;

        return false;
    }

    public EnumFacing getSupportDirection(IBlockState state){
        Block block = state.getBlock();

        if (block instanceof BlockTorch)
            return state.getValue(BlockTorch.FACING);

        if (block instanceof BlockButton)
            return state.getValue(BlockDirectional.FACING);

        if (block instanceof BlockLever)
            return state.getValue(BlockLever.FACING).getFacing();

        if (block instanceof BlockTripWireHook)
            return state.getValue(BlockHorizontal.FACING);

        if (supportsUp(state))
            return EnumFacing.UP;

        return null;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        EntityPlayer player = (EntityPlayer)entity;
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        doUpdate(stack, player, player.inventory, itemSlot, nbt.getBoolean("active"));
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack stack, EntityLivingBase entity) {
        EntityPlayer player = (EntityPlayer) entity;
        if (player == null)
            return;
        int itemSlot = Common.isBaubleEquipped(player, ModItems.earthStrider);
        IInventory inventory = new BaublesInventoryWrapper(BaublesApi.getBaublesHandler(player));
        doUpdate(stack, player, inventory, itemSlot, true);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.AMULET;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return newStack.getItem() != ModItems.earthStrider;
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}
