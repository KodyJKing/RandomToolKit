package rtk.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemDevTool extends ItemBase {
    public ItemDevTool(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote)
            return EnumActionResult.SUCCESS;

        IBlockState state = world.getBlockState(pos);
        world.setBlockToAir(pos);

        EntityFallingBlock entity = new EntityFallingBlock(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state);;
        world.spawnEntityInWorld(entity);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    public static boolean documentTileEntity(BlockPos pos, EntityPlayer player){
        TileEntity te = player.worldObj.getTileEntity(pos);
        if(te == null)
            return false;

        documentNBT(te.writeToNBT(new NBTTagCompound()), player);
        return true;
    }

    public static boolean documentItem(EntityPlayer player){
        ItemStack otherStack = player.inventory.getStackInSlot(player.inventory.currentItem + 1);
        if(otherStack == null || !otherStack.hasTagCompound())
            return false;

        documentNBT(otherStack.getTagCompound(), player);
        return true;
    }

    public static void documentNBT(NBTTagCompound nbt, EntityPlayer player){
        String nbtText = nbt.toString();
        if(player.worldObj.isRemote)
            nbtText = "CLIENT: " + nbtText;
        else
            nbtText = "SERVER: " + nbtText;
        player.addChatComponentMessage(new TextComponentString(nbtText));
    }
}
