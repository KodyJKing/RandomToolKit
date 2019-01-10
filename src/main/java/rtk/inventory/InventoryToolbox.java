package rtk.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import rtk.common.CNBT;
import rtk.item.ItemToolbox;

public class InventoryToolbox extends InventoryStack {

    public InventoryToolbox(ItemStack stack, EntityPlayer player, int stackIndex) {
        super(stack, player, stackIndex);
    }

    // TODO: Implement
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    protected NBTTagCompound getNBT() {
        ItemToolbox item = (ItemToolbox) stack.getItem();
        if (item.isEnder) {
            NBTTagCompound playerData = player.getEntityData();
            NBTTagCompound persist = CNBT.ensureCompound(playerData, EntityPlayer.PERSISTED_NBT_TAG);
            return CNBT.ensureCompound(persist, "rtk:toolBox");
        } else {
            return stack.getTagCompound();
        }
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
    public void openInventory(EntityPlayer player) {
        if (!player.world.isRemote) {
            player.world.playSound(
                    null,
                    player.posX, player.posY, player.posZ,
                    SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS,
                    0.3F, 1.5F);
        }
        stack.getTagCompound().setBoolean("open", true);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.world.isRemote) {
            player.world.playSound(
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
    public String getName() {
        return "toolBox";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("item.toolbox.name");
    }
}
