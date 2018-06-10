package rtk.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class TeleporterExitLightVoid implements ITeleporter {
    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        entity.fallDistance = -255;
        entity.setPositionAndRotation(entity.posX, 254, entity.posZ, yaw, entity.rotationPitch);

        EntityPlayer player = (EntityPlayer) entity;
        if (player != null)
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 240));
    }
}
