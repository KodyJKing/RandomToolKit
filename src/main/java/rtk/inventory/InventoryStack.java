package rtk.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rtk.common.CNBT;

import javax.annotation.Nullable;

public abstract class InventoryStack implements IInventory {

    public ItemStack stack;
    public int stackIndex;
    ItemStack[] inventory;

    public InventoryStack(ItemStack stack, int stackIndex){
        this.stack = stack;
        this.stackIndex = stackIndex;
        CNBT.ensureCompound(stack);
        inventory = new ItemStack[getSizeInventory()];
        loadAll();
    }

    public InventoryStack(ItemStack stack){
        this(stack, -1);
    }

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

    public void onChange(){}

    @Override
    public void markDirty() {
        saveAll();
        onChange();
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
        onChange();
        return result;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        loadAt(index);
        ItemStack result = ItemStackHelper.getAndRemove(inventory, index);
        saveAt(index);
        onChange();
        return result;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        inventory[index] = stack;
        saveAt(index);
        onChange();
    }

    @Override
    public void clear() {
        inventory = new ItemStack[getSizeInventory()];
        saveAll();
        onChange();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    protected NBTTagCompound getNBT(){
        return stack.getTagCompound();
    }

    protected NBTTagCompound getNBTAt(int i){
        return getInventoryList().getCompoundTagAt(i);
    }

    protected NBTTagList getInventoryList(){
        return getNBT().getTagList("inventory", 10);
    }

    //Boiler plate:

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }


    @Override
    public boolean hasCustomName() {
        return false;
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
}
