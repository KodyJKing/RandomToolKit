package rtk.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import rtk.common.CMath;

public class TeleporterRTK extends Teleporter {
    WorldServer world;

    public TeleporterRTK(WorldServer worldIn) {
        super(worldIn);
        world = worldIn;
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
        int x = MathHelper.floor(entity.posX);
        int z = MathHelper.floor(entity.posZ);

        BlockPos pos = new BlockPos(x, 64, z);
//        world.setBlockState(pos, Blocks.STONE.getDefaultState());
        for (BlockPos p: CMath.cuboid( pos.add(-1,0,-1), pos.add(1, 0, 1) ))
            world.setBlockState(p, Blocks.STONE.getDefaultState());

        entity.setVelocity(0, 0, 0);
        entity.setPosition(x + 0.5, 95, z + 0.5);

        EntityPlayer player = (EntityPlayer) entity;
        if (player != null) {
            player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 120));
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 120));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120));
        }

        return true;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
        return true;
    }
}
