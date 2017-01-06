package rtk.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.inventory.InventoryUltraDispenser;

public class UltraDispenserHandler {

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

    public static void tryFire(ItemStack stack, Vec3d pos, Vec3d heading, World world, EntityPlayer player){
        if(world.isRemote)
            return;

        InventoryUltraDispenser inv = new InventoryUltraDispenser(stack);
        int power = getPower(inv.getStackInSlot(0));

        if(power > 0)
            inv.decrStackSize(0, 1);

        EntityArrow arrow = new EntityTippedArrow(world, pos.xCoord, pos.yCoord, pos.zCoord);
        arrow.shootingEntity = player;
        arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
        arrow.setThrowableHeading(heading.xCoord, heading.yCoord, heading.zCoord, 3 + power * 0.000625F , 0);
        world.spawnEntityInWorld(arrow);
    }
}
