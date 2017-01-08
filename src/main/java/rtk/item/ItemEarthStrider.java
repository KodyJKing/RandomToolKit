package rtk.item;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import rtk.ModBlocks;
import rtk.ModItems;
import rtk.block.BlockHole;
import rtk.common.CNBT;
import rtk.common.Common;
import rtk.tileentity.TileHole;

public class ItemEarthStrider extends ItemBase {
    public ItemEarthStrider(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
        setMaxDamage(10000);
    }

    public NBTTagCompound ensureTag(ItemStack stack){
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        if(!nbt.hasKey("active"))
            nbt.setBoolean("active", false);
        return nbt;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        NBTTagCompound nbt = ensureTag(stack);
        nbt.setBoolean("active", !nbt.getBoolean("active"));

        if(!world.isRemote)
            player.addChatComponentMessage(new TextComponentTranslation(nbt.getBoolean("active")? "item.earthStrider.active" : "item.earthStrider.inactive"));

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return ensureTag(stack).getBoolean("active");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if(!ensureTag(stack).getBoolean("active") || !(entity instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer)entity;
        Vec3d center = Common.getTrueCenter(player);
        BlockPos foot = new BlockPos(center.xCoord, player.posY + 0.5, center.zCoord);

        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        if(player.isSneaking()){
            if(!nbt.getBoolean("alreadySneaking"))
                foot = foot.add(0, -1 ,0);
            nbt.setBoolean("alreadySneaking", true);
        } else {
            nbt.setBoolean("alreadySneaking", false);
        }

        int holeVolume = 0;

        int edgeDist = 7;
        for(BlockPos pos : Common.cuboid(foot.add(-edgeDist, 0, -edgeDist), foot.add(edgeDist, 3, edgeDist))){
            int dx = pos.getX() - foot.getX();
            int dy = pos.getY() - foot.getY();
            int dz = pos.getZ() - foot.getZ();
            dx = Math.abs(dx);
            dz = Math.abs(dz);

            boolean isEdge = dx == edgeDist || dy == 3 || dz == edgeDist;

            IBlockState state = world.getBlockState(pos);
            if(state.getBlock() instanceof BlockHole){
                TileHole hole = (TileHole) world.getTileEntity(pos);
                if(!isEdge){
                    hole.setTimeLeft(5);
                    holeVolume++;
                } else {
                    hole.replace(true);
                }
            }
            if(world.isRemote || !(state.getBlock() instanceof BlockStone) || isEdge || !canMakeHole(world, pos))
                continue;

            world.setBlockState(pos, ModBlocks.hole.getDefaultState());
            TileHole hole = (TileHole) world.getTileEntity(pos);
            hole.setPrevState(state);
        }

        if(holeVolume >= 200 && !player.capabilities.isCreativeMode){
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40));
            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 40));
            stack.setItemDamage(stack.getItemDamage() + 1);
            if(stack.getItemDamage() >= stack.getMaxDamage()){
                player.inventory.setInventorySlotContents(itemSlot, new ItemStack(ModItems.earthStriderDrained));
                player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 1);
            }
        }
    }

    public boolean canMakeHole(World world, BlockPos pos){
        for(EnumFacing dir : EnumFacing.VALUES){
            BlockPos n = pos.offset(dir);
            IBlockState state = world.getBlockState(n);
            if(state.getBlock() instanceof BlockLiquid || getSupportDirection(state) == dir)
                return false;
        }

        return true;
    }

    public boolean supportsUp(IBlockState state){
        Block block = state.getBlock();

        if(block == Blocks.GRAVEL || block == Blocks.SAND)
            return true;
        if(block instanceof BlockDoor || block instanceof BlockRailBase)
            return true;
        if(block instanceof BlockRedstoneDiode || block instanceof BlockRedstoneWire)
            return true;

        return false;
    }

    public EnumFacing getSupportDirection(IBlockState state){
        Block block = state.getBlock();

        if(block instanceof BlockTorch)
            return state.getValue(BlockTorch.FACING);

        if(block instanceof BlockButton)
            return state.getValue(BlockDirectional.FACING);

        if(block instanceof BlockLever)
            return state.getValue(BlockLever.FACING).getFacing();

        if(block instanceof BlockTripWireHook)
            return state.getValue(BlockHorizontal.FACING);

        if(supportsUp(state))
            return EnumFacing.UP;

        return null;
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }
}
