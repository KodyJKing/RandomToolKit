package rtk.common;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;

public abstract class UDispenseBehavior{

    private static final HashMap<Integer, UDispenseBehavior> behaviors = new HashMap<Integer, UDispenseBehavior>();

    private final Item payloadType;
    private final Item modifierType;

    public static final int MASK_12 = (1 << 12) - 1;

    final static int key(Item payload, Item modifier){
        return ((Item.getIdFromItem(payload) & MASK_12) << 12) | (Item.getIdFromItem(modifier) & MASK_12);
    }

    final int key(){
        return key(payloadType, modifierType);
    }

    public static UDispenseBehavior get(Item payload, Item modifier){
        int key = key(payload, modifier);
        if(behaviors.containsKey(key))
            return behaviors.get(key);

        key = key(payload, null);
        if(behaviors.containsKey(key))
            return behaviors.get(key);

        return null;
    }

    public static void add(UDispenseBehavior behavior){
        behaviors.put(behavior.key(), behavior);
    }

    // -----------------------------------------

    public UDispenseBehavior(Item payload, Item modifier){
        this.payloadType = payload;
        this.modifierType = modifier;
    }

    public UDispenseBehavior(Item payload){
        this(payload, null);
    }

    public boolean fire(World world, Vec3d pos, Vec3d heading, EntityPlayer player, int power, Item payload, Item modifier){return true;}

    public int fuelCost(Item fuel, Item modifier){return fuel == null ? 0 : 1;}
    public int payloadCost(Item modifier){return 1;}
    public int modifierCost(Item modifier){return 0;}

    public int fireCount(Item modifier){return 1;}


    // -----------------------------------------


    public static void registerBehaviors(){
        add(new UDispenseBehavior(Items.ARROW) {
            @Override
            public boolean fire(World world, Vec3d pos, Vec3d heading, EntityPlayer player, int power, Item payload, Item modifier) {
                float inaccuracy = modifier == Items.STRING ? 15 : 0;

                EntityArrow arrow = new EntityTippedArrow(world, pos.xCoord, pos.yCoord, pos.zCoord);
                arrow.shootingEntity = player;
                arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                arrow.setThrowableHeading(heading.xCoord, heading.yCoord + (player == null ? 0.1 : 0), heading.zCoord, 2 + power * 0.0003F , inaccuracy);
                world.spawnEntityInWorld(arrow);

                world.playSound(null, pos.xCoord, pos.yCoord, pos.zCoord, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 0.7F, 0.5F + Common.random.nextFloat());

                return true;
            }

            @Override
            public int fireCount(Item modifier) {
                return modifier == Items.STRING ? 10 : 1;
            }
        });

        add(new UDispenseBehavior(Item.getItemFromBlock(Blocks.TNT)) {
            @Override
            public boolean fire(World world, Vec3d pos, Vec3d heading, EntityPlayer player, int power, Item payload, Item modifier) {
                float velocity = 2 + power * 0.0003F;
                heading = heading.scale(velocity);

                if(modifier == Items.STRING)
                {
                    heading = Common.randomVector(velocity, Math.PI / 6, heading);
                }

                EntityTNTPrimed tnt = new EntityTNTPrimed(world, pos.xCoord, pos.yCoord, pos.zCoord, player);
                tnt.setVelocity(heading.xCoord, heading.yCoord, heading.zCoord);
                tnt.setFuse(10);
                world.spawnEntityInWorld(tnt);

                return true;
            }

            @Override
            public int fireCount(Item modifier) {
                return modifier == Items.STRING ? 10 : 1;
            }
        });
    }
}