package rtk.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;
import rtk.common.CMath;

public class TeleporterExitDarkVoid implements ITeleporter {

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        EntityPlayer player = (EntityPlayer) entity;
        if (player != null)
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 240));

        int x = MathHelper.floor(entity.posX);
        int z = MathHelper.floor(entity.posZ);

        BlockPos pos = new BlockPos(x, 0, z);

        BlockPos bedRockHole = findBedrockHole(world, pos);
        if (bedRockHole == null)
            return;
        BlockPos spawnPos = findSpawnLocationByHole(world, bedRockHole);
        if (spawnPos == null)
            return;

        entity.motionX = 0;
        entity.motionY = 0;
        entity.motionZ = 0;
        entity.setPosition(spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5);
    }

    public BlockPos findBedrockHole(World world, BlockPos pos) {
        int radius = 16;
        BlockPos result = null;
        for (BlockPos p: CMath.cuboid( pos.add(-radius,0, -radius), pos.add(radius, 0, radius) )) {
            if (world.isAirBlock(p)) {
                result = p;
                break;
            }
        }

        if (result == null)
            return null;

        // Move away from edges to center in 3x3 hole.
        for (EnumFacing dir: EnumFacing.HORIZONTALS)
            if (!world.isAirBlock(result.offset(dir)))
                result = result.offset(dir, -1);

        return result;
    }

    public BlockPos findSpawnLocationByHole(World world, BlockPos pos) {
        int radius = 2;
        int height = 6;
        for (BlockPos p: CMath.cuboid( pos.add(-radius,0, -radius), pos.add(radius, height, radius) )) {
            if (world.isSideSolid(p, EnumFacing.UP) && world.isAirBlock(p.up(1)) && world.isAirBlock(p.up(2)))
                return p;
        }
        return null;
    }
}
