package rtk.udispenser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
public class TileUDispenser extends TileEntity implements IInventory{

    private ItemStack[] inventory;
    private boolean powered = false;

    public TileUDispenser() {
        super();
        inventory = new ItemStack[getSizeInventory()];
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList nbtList = new NBTTagList();
        for(int i = 0; i < getSizeInventory(); i++){
            NBTTagCompound itemNBT = new NBTTagCompound();
            if(inventory[i] != null)
                inventory[i].writeToNBT(itemNBT);
            nbtList.appendTag(itemNBT);
        }
        compound.setTag("inventory", nbtList);

        compound.setBoolean("powered", powered);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList nbtList = compound.getTagList("inventory", 10);
        for(int i = 0; i < getSizeInventory(); i++)
            inventory[i] = ItemStack.loadItemStackFromNBT(nbtList.getCompoundTagAt(i));

        powered = compound.getBoolean("powered");
    }

    @Override
    public int getSizeInventory() {
        return 11;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(inventory, index, count);
        if(stack != null)
            markDirty();
        return stack;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = ItemStackHelper.getAndRemove(inventory, index);
        if(stack != null)
            markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        inventory[index] = stack;
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if(worldObj.isRemote)
            clear();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != 0 || UDispenser.isItemFuel(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for(int i = 0; i < getSizeInventory(); i++)
            inventory[i] = null;
        markDirty();
    }

    @Override
    public String getName() {
        return "ultraDispenser";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("tile.ultraDispenser.name");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
