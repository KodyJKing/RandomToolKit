package rtk.item;

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
        TileEntity te = world.getTileEntity(pos);
        if(te == null)
            return EnumActionResult.PASS;
        String nbtText = te.writeToNBT(new NBTTagCompound()).toString();
        if(world.isRemote)
            nbtText = "CLIENT: " + nbtText;
        else
            nbtText = "SERVER: " + nbtText;
        player.addChatComponentMessage(new TextComponentString(nbtText));
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        ItemStack otherStack = player.inventory.getStackInSlot(player.inventory.currentItem + 1);
        if(otherStack == null || !otherStack.hasTagCompound())
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);

        String nbtText = otherStack.getTagCompound().toString();
        if(world.isRemote)
            nbtText = "CLIENT: " + nbtText;
        else
            nbtText = "SERVER: " + nbtText;
        player.addChatComponentMessage(new TextComponentString(nbtText));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
