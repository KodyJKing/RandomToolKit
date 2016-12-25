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

public class InventoryToolbox implements IInventory {

    public ItemStack stack;

    public InventoryToolbox(ItemStack stack){
        this.stack = stack;
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        if(!nbt.hasKey("inventory"))
            clear();
    }

    private NBTTagList getInventoryList(){
        return stack.getTagCompound().getTagList("inventory", 10);
    }

    private NBTTagCompound NBTAt(int i){
        return getInventoryList().getCompoundTagAt(i);
    }

    @Override
    public int getSizeInventory() {
        return 54;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        NBTTagCompound nbt = NBTAt(index);
        return ItemStack.loadItemStackFromNBT(nbt);
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(count > 0)
            return removeStackFromSlot(index);
        return null;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack result = getStackInSlot(index);
        setInventorySlotContents(index, null);
        return result;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack otherStack) {
        NBTTagCompound nbt = new NBTTagCompound();
        if(otherStack != null)
            otherStack.writeToNBT(nbt);
        getInventoryList().set(index, nbt);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {}

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
        stack = player.inventory.getStackInSlot(stack.getTagCompound().getInteger("index"));

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
    public void clear() {
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < getSizeInventory(); i++)
            list.appendTag(new NBTTagCompound());
        nbt.setTag("inventory", list);
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
