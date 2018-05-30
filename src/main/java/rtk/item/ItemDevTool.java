package rtk.item;

import net.minecraft.entity.EntityLivingBase;
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
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        super.onUsingTick(stack, player, count);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return documentTileEntity(pos, player) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        return new ActionResult<>(documentItem(player) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL, stack);
    }

    public static boolean documentTileEntity(BlockPos pos, EntityPlayer player){
        TileEntity te = player.world.getTileEntity(pos);
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
        if(player.world.isRemote)
            nbtText = "CLIENT: " + nbtText;
        else
            nbtText = "SERVER: " + nbtText;
        player.sendMessage(new TextComponentString(nbtText));
    }
}
