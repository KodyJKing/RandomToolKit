package rtk.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public abstract class InventoryNBT implements IInventory {

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        NBTTagCompound nbt = getNBTAt(index);
        return ItemStack.loadItemStackFromNBT(nbt);
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = getStackInSlot(index);
        if(stack.stackSize == 0)
            return null;

        ItemStack stackOut = stack.copy();

        if(stack.stackSize <= count){
            stackOut.stackSize = stack.stackSize;
            removeStackFromSlot(index);
        } else {
            stackOut.stackSize = count;
            stack.stackSize -= count;
            setInventorySlotContents(index, stack);
        }

        return stackOut;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack result = getStackInSlot(index);
        setInventorySlotContents(index, null);
        return result;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        NBTTagCompound nbt = new NBTTagCompound();
        if(stack != null)
            stack.writeToNBT(nbt);
        getInventoryList().set(index, nbt);
    }

    @Override
    public void clear() {
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < getSizeInventory(); i++)
            list.appendTag(new NBTTagCompound());
        getNBT().setTag("inventory", list);
    }

    protected abstract NBTTagCompound getNBT();

    protected NBTTagCompound getNBTAt(int i){
        return getInventoryList().getCompoundTagAt(i);
    }

    protected NBTTagList getInventoryList(){
        return getNBT().getTagList("inventory", 10);
    }

    //Boiler plate:

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
}
