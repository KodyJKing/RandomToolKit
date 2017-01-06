package rtk.inventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.Sys;
import rtk.common.CNBT;
import rtk.item.ItemUltraDispenser;
import rtk.tileentity.TileUltraDispenser;

import javax.annotation.Nullable;

public class InventoryUltraDispenser extends InventoryNBT {

    public ItemStack stack;
    public int stackIndex;

    TileUltraDispenser tile;

    public InventoryUltraDispenser(ItemStack stack, int stackIndex){
        this.stack = stack;
        this.stackIndex = stackIndex;
        CNBT.ensureCompound(stack);
        loadAll();
    }

    public InventoryUltraDispenser(TileUltraDispenser tile){
        stackIndex = -1;
        this.tile = tile;
        loadAll();
    }

    public InventoryUltraDispenser(ItemStack stack){
        this(stack, -1);
    }

    @Override
    protected NBTTagCompound getNBT() {
        return tile == null ? stack.getTagCompound() : tile.data;
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if(player.worldObj.isRemote)
            clear();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
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
        return "ultraDispenser";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("tile.ultraDispenser.name");
    }

    @Override
    public void onChange() {
        if(tile == null)
            return;

        tile.markDirty();
    }
}
