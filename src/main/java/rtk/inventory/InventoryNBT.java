package rtk.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;

public abstract class InventoryNBT implements IInventory {

    ItemStack[] inventory = new ItemStack[getSizeInventory()];

    public void loadAll(){
        if(!getNBT().hasKey("inventory")){
            NBTTagList list = new NBTTagList();
            for(int i = 0; i < getSizeInventory(); i++)
                list.appendTag(new NBTTagCompound());
            getNBT().setTag("inventory", list);
        }

        for(int i = 0; i < inventory.length; i++)
            loadAt(i);
    }

    public void saveAll(){
        for(int i = 0; i < inventory.length; i++)
            saveAt(i);
    }

    public void loadAt(int index){
        inventory[index] = ItemStack.loadItemStackFromNBT(getNBTAt(index));
    }

    public void saveAt(int index){
        NBTTagCompound nbt = new NBTTagCompound();
        if(inventory[index] != null)
            inventory[index].writeToNBT(nbt);
        getInventoryList().set(index, nbt);
    }

    @Override
    public void markDirty() {
        saveAll();
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        loadAt(index);
        return inventory[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        loadAt(index);
        ItemStack result = ItemStackHelper.getAndSplit(inventory, index, count);
        saveAt(index);
        return result;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        loadAt(index);
        ItemStack result = ItemStackHelper.getAndRemove(inventory, index);
        saveAt(index);
        return result;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        inventory[index] = stack;
        saveAt(index);
    }

    @Override
    public void clear() {
        inventory = new ItemStack[getSizeInventory()];
        saveAll();
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
