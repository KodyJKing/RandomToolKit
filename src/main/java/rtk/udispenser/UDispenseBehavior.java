package rtk.udispenser;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.common.Common;
import rtk.entity.EntityRtkArrow;
import rtk.entity.EntityRtkTNT;

import java.util.HashMap;

public abstract class UDispenseBehavior{

    private static final HashMap<Item, UDispenseBehavior> behaviors = new HashMap<Item, UDispenseBehavior>();

    private final Item payloadType;

    public static UDispenseBehavior get(Item payload){
        if(behaviors.containsKey(payload))
            return behaviors.get(payload);
        return null;
    }

    public static void add(UDispenseBehavior behavior){
        behaviors.put(behavior.payloadType, behavior);
    }

    public UDispenseBehavior(Item payload){
        this.payloadType = payload;
    }

    // -----------------------------------------

    public abstract void dispense(World world, Vec3d pos, Vec3d heading, EntityPlayer player, UDispenser dispenser);

    // -----------------------------------------


    public static void registerBehaviors(){
        add(new UDispenseBehaviorProjectile(Items.ARROW) {
            @Override
            public void fire(World world, Vec3d pos, Vec3d heading, EntityPlayer player, UDispenser dispenser) {

                EntityRtkArrow arrow = new EntityRtkArrow(world, pos.xCoord, pos.yCoord, pos.zCoord);
                arrow.shootingEntity = player;
                arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                arrow.setThrowableHeading(heading.xCoord, heading.yCoord + (player == null ? 0.1 : 0), heading.zCoord, 2 + dispenser.getPower() * 0.0003F , 0);

                if(dispenser.hasModifier(Item.getItemFromBlock(Blocks.TNT)))
                {
                    arrow.explodeOnHit = true;
                    dispenser.spendModifier(Item.getItemFromBlock(Blocks.TNT), 1);
                }

                world.spawnEntityInWorld(arrow);
            }
        });

        add(new UDispenseBehaviorProjectile(Item.getItemFromBlock(Blocks.TNT)) {
            @Override
            public void fire(World world, Vec3d pos, Vec3d heading, EntityPlayer player, UDispenser dispenser) {
                float velocity = 2 + dispenser.getPower() * 0.0003F;
                heading = heading.scale(velocity);

                EntityRtkTNT tnt = new EntityRtkTNT(world, pos.xCoord + heading.xCoord, pos.yCoord + heading.yCoord, pos.zCoord + heading.zCoord, player);
                tnt.setVelocity(heading.xCoord, heading.yCoord, heading.zCoord);

                int fuse = 8 + Common.random.nextInt(3);
                if(dispenser.hasModifier(Items.STRING))
                    fuse *= 2;
                else if(dispenser.hasModifier(Items.SHEARS))
                    fuse /= 2;

                if(dispenser.hasModifier(Item.getItemFromBlock(Blocks.STONE_BUTTON)))
                    tnt.explodeOnImpact = true;

                tnt.setFuse(fuse);
                world.spawnEntityInWorld(tnt);
            }
        });

        add(new UDispenseBehavior(Items.BEEF) {
            @Override
            public void dispense(World world, Vec3d pos, Vec3d heading, EntityPlayer player, UDispenser dispenser) {

                if(!dispenser.hasPayload(8))
                    return;

                double velocity = 2 + dispenser.getPower() * 0.0003F;
                heading = heading.scale(velocity);
                EntityCow cow = new EntityCow(world);
                cow.setLocationAndAngles(pos.xCoord, pos.yCoord, pos.zCoord, player != null ? player.rotationYaw : 0, 0.0F);
                cow.setVelocity(heading.xCoord, heading.yCoord, heading.zCoord);
                world.spawnEntityInWorld(cow);

                dispenser.spendPayload(8);
                dispenser.spendFuel(1);

                world.playSound(null, pos.xCoord, pos.yCoord, pos.zCoord, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 0.7F, 0.5F + Common.random.nextFloat());
                world.playSound(null, pos.xCoord, pos.yCoord, pos.zCoord, SoundEvents.ENTITY_COW_HURT, SoundCategory.NEUTRAL, 0.7F, 0.5F + Common.random.nextFloat());
            }
        });
    }
}