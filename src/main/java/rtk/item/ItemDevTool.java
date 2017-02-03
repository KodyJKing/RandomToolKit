package rtk.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import rtk.entity.EntityEyeOfNether;

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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return documentTileEntity(pos, player) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        return new ActionResult<ItemStack>(documentItem(player) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL, stack);
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
