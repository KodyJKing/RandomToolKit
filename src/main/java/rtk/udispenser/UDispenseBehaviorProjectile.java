package rtk.udispenser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.common.Common;

public abstract class UDispenseBehaviorProjectile extends UDispenseBehavior {
    public UDispenseBehaviorProjectile(Item payload) {
        super(payload);
    }

    @Override
    public void dispense(World world, Vec3d pos, Vec3d heading, EntityPlayer player, UDispenser dispenser) {
        for(int i = 0; i < repeats(dispenser); i++){
            if(!dispenser.hasPayload(1))
                return;
            fire(world, pos, Common.randomVector(heading, 1, inaccuracy(dispenser)), player, dispenser);
            dispenser.spendFuel(1);
            dispenser.spendPayload(1);

            world.playSound(null, pos.xCoord, pos.yCoord, pos.zCoord, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 0.7F, 0.5F + Common.random.nextFloat());
        }
    }

    public int repeats(UDispenser dispenser){
        return dispenser.hasModifier(Items.PAPER) ? 8 : 1;
    }

    public double inaccuracy(UDispenser dispenser){
        double spread = dispenser.hasModifier(Items.FEATHER) ? 0.25 : 1;
        if(dispenser.hasModifier(Items.PAPER))
            spread *= 16;
        return spread;
    }

    public abstract void fire(World world, Vec3d pos, Vec3d heading, EntityPlayer player, UDispenser dispenser);
}
