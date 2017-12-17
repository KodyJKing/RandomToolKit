package rtk.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import rtk.common.CNBT;
import rtk.common.CWorld;
import rtk.common.Common;

import java.util.ArrayList;
import java.util.List;

public class ItemTrowel extends ItemBase {

    static int maxLength = 9;

    public ItemTrowel(String name){
        super(name);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    public ItemStack getBuildingStack(EntityPlayer player){
        int nextItem = player.inventory.currentItem + 1;
        if(nextItem > 35)
            return null;
        return player.inventory.getStackInSlot(nextItem);
    }

    public boolean useBlock(EntityPlayer player){
        IInventory inv = player.inventory;
        int nextItem = player.inventory.currentItem + 1;
        ItemStack stack = getBuildingStack(player);
        if( stack == null || Block.getBlockFromItem(stack.getItem()) == Blocks.AIR )
            return false;

        if(!player.capabilities.isCreativeMode)
            inv.decrStackSize(nextItem, 1);

        if(stack.getCount() == 0)
            tryRefill(player, stack, nextItem);
        return true;
    }

    public void tryRefill(EntityPlayer player, ItemStack stack, int slot){
        ItemStack refill = Common.getRefill(player, stack, 64);
        if(refill != null)
            player.inventory.setInventorySlotContents(slot, refill);
    }

    public void buildOrSelect(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, EnumFacing facing, List<BlockPos> selection){

        boolean live = selection == null;

//Old 8 direction movement.
//        float adjust = (float)Math.PI / 180.0F;
//        int dx = (int)Math.floor(-Math.sin(player.rotationYaw * adjust) + 0.5F);
//        int dz = (int)Math.floor(Math.cos(player.rotationYaw * adjust) + 0.5F);

        float adjust = (float)Math.PI / 180.0F;
        double angle = Math.round(player.rotationYaw / 90) * 90;
        int dx = (int)Math.round(-Math.sin(angle * adjust));
        int dz = (int)Math.round(Math.cos(angle * adjust));

        if(player.isSneaking())
        {
            x += dx;
            z += dz;
        } else {
            x += facing.getFrontOffsetX();
            y += facing.getFrontOffsetY();
            z += facing.getFrontOffsetZ();
        }

        IBlockState bs = null;

        if(live){
            ItemStack material = getBuildingStack(player);
            if(material == null)
                return;
            Block block = Block.getBlockFromItem(material.getItem());
            if(block == null)
                return;
//            bs = block.onBlockPlaced(world, new BlockPos(x, y, z), facing, x, y, z, material.getMetadata(), player);
            bs = block.getDefaultState();
        }

        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        int length = CNBT.ensureInt(nbt, "length", 1);
        for(int i = 0; i < length; i++){
            BlockPos pos = new BlockPos(x, y, z);

            if(!CWorld.shouldReplace(world, pos) || live && !useBlock(player))
                break;

            if(live)
                world.setBlockState(pos, bs, 3);
            else
                selection.add(pos);

            x += dx;
            z += dz;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(player.isSneaking() && !world.isRemote){
            NBTTagCompound nbt = CNBT.ensureCompound(stack);
            int length = CNBT.ensureInt(nbt, "length", 1) + 1;

            if(length > maxLength)
                length = 1;

            player.sendMessage(new TextComponentString(Integer.toString(length)));
            player.swingArm(hand);

            nbt.setInteger("length", length);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        buildOrSelect(stack, player, world, pos.getX(), pos.getY(), pos.getZ(), facing, null);
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity e, int itemSlot, boolean isSelected) {
        if(!isSelected || !worldIn.isRemote)
            return;

        EntityPlayer p = (EntityPlayer)e;
        if(p == null)
            return;

        if(Minecraft.getMinecraft().objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
            return;

        BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
        if(worldIn.getBlockState(pos).getMaterial() == Material.AIR)
            return;

        EnumFacing side = Minecraft.getMinecraft().objectMouseOver.sideHit;
        double dist = pos.distanceSqToCenter(e.posX, e.posY, e.posZ);
        if(dist > 20.25F)
            return;

        ArrayList<BlockPos> selection = new ArrayList<BlockPos>();
        buildOrSelect(stack, p, worldIn, pos.getX(), pos.getY(), pos.getZ(), side, selection);

        for(BlockPos s : selection){
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, s.getX() + 0.5, s.getY() + 0.5, s.getZ() + 0.5, 0, 0, 0);
        }
    }
}
