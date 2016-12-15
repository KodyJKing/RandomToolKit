package rtk.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import rtk.common.CNBT;

public class ItemTrowel extends ItemBase {

    static int maxLength = 5;

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
        return true;
    }

    public void build(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, EnumFacing facing){

        x += facing.getFrontOffsetX();
        y += facing.getFrontOffsetY();
        z += facing.getFrontOffsetZ();

        ItemStack material = getBuildingStack(player);
        if(material == null)
            return;
        Block block = Block.getBlockFromItem(material.getItem());
        if(block == null)
            return;
        IBlockState bs = block.onBlockPlaced(world, new BlockPos(x, y, z), facing, x, y, z, material.getMetadata(), player);

        float adjust = (float)Math.PI / 180.0F;
        int dx = (int)Math.floor(-Math.sin(player.rotationYaw * adjust) + 0.5F);
        int dz = (int)Math.floor(Math.cos(player.rotationYaw * adjust) + 0.5F);

        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        int length = CNBT.ensureInt(nbt, "length", 1);
        for(int i = 0; i < length; i++){
            BlockPos pos = new BlockPos(x, y, z);

            if(world.getBlockState(pos).isOpaqueCube() || !useBlock(player))
                break;

            world.setBlockState(pos, bs, 3);

            x += dx;
            z += dz;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(player.isSneaking() && !world.isRemote){
            NBTTagCompound nbt = CNBT.ensureCompound(stack);
            int length = CNBT.ensureInt(nbt, "length", 1) + 1;

            if(length > maxLength)
                length = 1;

            player.addChatComponentMessage(new TextComponentString(Integer.toString(length)));
            player.swingArm(hand);

            nbt.setInteger("length", length);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        build(stack, player, world, pos.getX(), pos.getY(), pos.getZ(), facing);
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }
}
