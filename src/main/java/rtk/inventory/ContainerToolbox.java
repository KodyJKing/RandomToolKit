package rtk.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import rtk.common.Common;

import javax.annotation.Nullable;

public class ContainerToolbox extends Container {
    private final InventoryToolbox toolboxInv;
    private final int numRows;

    public ContainerToolbox(IInventory playerInventory, InventoryToolbox chestInventory, EntityPlayer player)
    {
        this.toolboxInv = chestInventory;
        this.numRows = chestInventory.getSizeInventory() / 9;
        chestInventory.openInventory(player);
        int i = (this.numRows - 4) * 18;

        for (int row = 0; row < this.numRows; ++row)
        {
            for (int column = 0; column < 9; ++column)
            {
                this.addSlotToContainer(new SlotToolbox(chestInventory, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 3; ++row)
        {
            for (int column = 0; column < 9; ++column)
            {
                this.addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + i));
            }
        }

        for (int column = 0; column < 9; ++column)
        {
            Slot slot;
            if(column == toolboxInv.stackIndex)
                slot = new SlotLocked(playerInventory, column, 8 + column * 18, 161 + i);
            else
                slot = new Slot(playerInventory, column, 8 + column * 18, 161 + i);
            this.addSlotToContainer(slot);
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.toolboxInv.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.numRows * 9)
            {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        this.toolboxInv.closeInventory(playerIn);
        super.onContainerClosed(playerIn);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getToolboxInv()
    {
        return this.toolboxInv;
    }
}
