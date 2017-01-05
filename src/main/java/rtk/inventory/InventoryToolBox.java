package rtk.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import rtk.common.CNBT;

import javax.annotation.Nullable;

public class InventoryToolbox extends InventoryNBT {

    public ItemStack stack;
    public int stackIndex;

    public InventoryToolbox(ItemStack stack, int stackIndex){
        this.stack = stack;
        this.stackIndex = stackIndex;
        CNBT.ensureCompound(stack);
        loadAll();
    }

    @Override
    protected NBTTagCompound getNBT() {
        return stack.getTagCompound();
    }

    @Override
    public int getSizeInventory() {
        return 54;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if(!player.worldObj.isRemote){
            player.worldObj.playSound(
                    null,
                    player.posX, player.posY, player.posZ,
                    SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS,
                    0.3F, 1.5F);
        }
        stack.getTagCompound().setBoolean("open", true);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if(!player.worldObj.isRemote){
            player.worldObj.playSound(
                    null,
                    player.posX, player.posY, player.posZ,
                    SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS,
                    0.3F, 1.5F);
        }

        //For some reason stack seems to point to a cloned object outside the player's inventory.
        stack = player.inventory.getStackInSlot(stackIndex);

        stack.getTagCompound().setBoolean("open", false);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack otherStack) {
        return otherStack.getMaxStackSize() == 1;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public String getName() {
        return "toolBox";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("item.toolbox.name");
    }
}
