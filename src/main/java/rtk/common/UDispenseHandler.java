package rtk.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.HashMap;

public class UDispenseHandler {

    private static final HashMap<Integer, UDispenseBehavior> behaviors = new HashMap<Integer, UDispenseBehavior>();
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
            return 40000;
        return TileEntityFurnace.getItemBurnTime(stack);
    }

    public static void tryFire(World world, Vec3d pos, Vec3d heading, IInventory inventory, EntityPlayer player){
        if(world.isRemote)
            return;

        ItemStack fuel = inventory.getStackInSlot(0);
        ItemStack payload = inventory.getStackInSlot(1);
        ItemStack modifier = inventory.getStackInSlot(2);

        if(payload == null)
            return;

        Item fuelItem = item(fuel);
        Item payItem = item(payload);
        Item modItem = item(modifier);

        UDispenseBehavior behavior = UDispenseBehavior.get(payItem, modItem);
        if(behavior == null)
            return;

        int power = getPower(fuel);
        for(int i = 0; i < behavior.fireCount(modItem); i++){
            boolean fired =
                    behavior.fuelCost(fuelItem, modItem) <= size(fuel)
                            && behavior.payloadCost(modItem) <= size(payload)
                            && behavior.modifierCost(modItem) <= size(modifier)
                            && behavior.fire(world, pos, heading, player, power, payItem, modItem);

            if(fired && (player == null || !player.capabilities.isCreativeMode)){
                inventory.decrStackSize(0, behavior.fuelCost(fuelItem, modItem));
                inventory.decrStackSize(1, behavior.payloadCost(modItem));
                inventory.decrStackSize(2, behavior.modifierCost(modItem));
            }
        }
    }

    static Item item(ItemStack stack){
        return stack == null ? null : stack.getItem();
    }

    static int size(ItemStack stack){
        return stack == null ? 0 : stack.stackSize;
    }
}
