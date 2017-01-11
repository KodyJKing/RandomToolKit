package rtk.udispenser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.common.Common;

public class UDispenser {
    IInventory inventory;
    boolean isCreative;
    EntityPlayer player;

    public UDispenser(IInventory inventory, EntityPlayer player) {
        this.inventory = inventory;
        this.isCreative = player != null && player.capabilities.isCreativeMode;
        this.player = player;
    }

    // -----------------------------------------

    public static boolean isItemFuel(ItemStack stack){
        return getPower(stack) > 0;
    }

    public static int getPower(ItemStack stack){
        if(stack == null)
            return 0;
        Item item = stack.getItem();
        if(item == Items.GUNPOWDER)
            return 8000;
        if(item == Item.getItemFromBlock(Blocks.TNT))
            return 16000;
        return TileEntityFurnace.getItemBurnTime(stack);
    }

    public int getPower(){
        return getPower(inventory.getStackInSlot(0));
    }

    // -----------------------------------------

    public static void tryDispense(World world, Vec3d pos, Vec3d heading, IInventory inventory, EntityPlayer player){
        if(world.isRemote)
            return;

        UDispenser dispenser = new UDispenser(inventory, player);

        Item payload = dispenser.getPayload();

        if(payload == null)
            return;

        UDispenseBehavior behavior = UDispenseBehavior.get(payload);
        if(behavior == null)
            return;

        behavior.dispense(world, pos, heading, player, dispenser);
    }

    // -----------------------------------------

    public boolean hasPayload(int amount){
        return size(1) >= amount;
    }

    public boolean hasModifier(Item item, int amount){
        for(int i = 2; i < 11; i++){
            if(item(i) == item && size(i) >= amount)
                return true;
        }
        return false;
    }

    public boolean hasModifier(Item item){
        return hasModifier(item, 1);
    }

    // -----------------------------------------

    public void spendFuel(int amount){
        if(!isCreative)
            spend(0, amount);
    }

    public void spendPayload(int amount){
        if(!isCreative)
            spend(1, amount);
    }

    public void spendModifier(Item item, int amount){
        if(isCreative)
            return;
        for(int i = 2; i < 11; i++){
            if(item(i) == item)
            {
                spend(i, amount);
                return;
            }
        }
    }

    public void spend(int index, int amount){
        ItemStack stack = inventory.getStackInSlot(index);
        inventory.decrStackSize(index, amount);
        tryRefill(index, stack);
    }

    public void tryRefill(int index, ItemStack stack){
        if(isCreative || player == null)
            return;
        int currentAmount = size(index);
        ItemStack refill = Common.getRefill(player, stack, 64 - currentAmount);
        if(refill == null)
            return;
        stack = stack.copy();
        stack.stackSize = currentAmount + refill.stackSize;
        inventory.setInventorySlotContents(index, stack);
    }

    // -----------------------------------------

    public Item getPayload(){
        return item(1);
    }
    public ItemStack getPayloadStack(){return inventory.getStackInSlot(1);}

    // -----------------------------------------

    private int size(int index){
        return size(inventory.getStackInSlot(index));
    }

    private int size(ItemStack stack){
        return stack == null ? 0 : stack.stackSize;
    }

    private Item item(int index){
        return item(inventory.getStackInSlot(index));
    }

    private Item item(ItemStack stack){
        return stack == null ? null : stack.getItem();
    }
}
