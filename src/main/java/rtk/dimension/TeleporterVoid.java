package rtk.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;
import rtk.common.CMath;

public class TeleporterVoid implements ITeleporter {
    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        int x = MathHelper.floor(entity.posX);
        int z = MathHelper.floor(entity.posZ);

        BlockPos pos = new BlockPos(x, 64, z);
        for (BlockPos p: CMath.cuboid( pos.add(-1,0,-1), pos.add(1, 0, 1) ))
            world.setBlockState(p, Blocks.STONE.getDefaultState());

        world.setBlockToAir(new BlockPos(x, 255, z));

        entity.motionX = 0;
        entity.motionY = 0;
        entity.motionZ = 0;
        entity.setPosition(x + 0.5, 280, z + 0.5);
        entity.fallDistance = -255;

        EntityPlayer player = (EntityPlayer) entity;
        if (player != null)
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 240));
    }
}
