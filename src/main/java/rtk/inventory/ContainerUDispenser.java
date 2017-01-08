package rtk.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import rtk.common.UDispenseHandler;

import javax.annotation.Nullable;

public class ContainerUDispenser extends Container {
    private final IInventory dispenserInventory;

    public ContainerUDispenser(IInventory playerInventory, IInventory dispenserInventoryIn)
    {
        this.dispenserInventory = dispenserInventoryIn;

        this.addSlotToContainer(new SlotFurnaceFuel(dispenserInventoryIn, 0, 26, 35){
            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return UDispenseHandler.isItemFuel(stack) || isBucket(stack);
            }
        });
        this.addSlotToContainer(new Slot(dispenserInventoryIn, 1, 80, 35));
        this.addSlotToContainer(new Slot(dispenserInventoryIn, 2, 134, 35));

        for (int row = 0; row < 3; ++row)
        {
            for (int column = 0; column < 9; ++column)
            {
                this.addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; ++column)
        {
            Slot slot;
            if(dispenserInventory instanceof InventoryStack && column == ((InventoryStack)dispenserInventory).stackIndex)
                slot = new SlotLocked(playerInventory, column, 8 + column * 18, 142);
            else
                slot = new Slot(playerInventory, column, 8 + column * 18, 142);
            this.addSlotToContainer(slot);
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.dispenserInventory.isUseableByPlayer(playerIn);
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

            if (index < 3)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 3, false))
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

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }
}
