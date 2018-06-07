package rtk.misc;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SkeletonRitualScanner {

    public static EnumFacing scan(World world, BlockPos pos) {
        for (EnumFacing finger: EnumFacing.HORIZONTALS) {
            EnumFacing thumb = finger.rotateY();

            if (!scanPillarPair(world, pos, finger, thumb,0, 3,3, 2 ))
                continue;
            if (!scanPillarPair(world, pos, finger, thumb, 2, 2,2, 2 ))
                continue;
            if (!scanPillarPair(world, pos, finger, thumb, -2, 3,2, 2 ))
                continue;
            if (!scanPillarPair(world, pos, finger, thumb,-4, 1,1, 1 ))
                continue;

            return finger;
        }

        return null;
    }

    public static boolean scanPillarPair(World world, BlockPos pos, EnumFacing finger, EnumFacing thumb, int df, int dt, int height, int width) {
        BlockPos posLeft = pos.offset(finger, df).offset(thumb, dt);
        BlockPos posRight = pos.offset(finger, df).offset(thumb, -dt);
        if (!scanPillar(world, posLeft, thumb.getOpposite(), height, width))
            return false;
        if (!scanPillar(world, posRight, thumb, height, width))
            return false;
        return true;
    }

    public static boolean scanPillar(World world, BlockPos pos, EnumFacing dir, int height, int width) {
        for (int dy = 0; dy < height; dy++) {
            BlockPos pos2 = pos.up(dy);
            if (world.getBlockState(pos2).getBlock() != Blocks.BONE_BLOCK)
                return false;
        }

        for (int dx = 1; dx < width; dx++) {
            BlockPos pos2 = pos.up(height - 1).offset(dir, dx);
            if (world.getBlockState(pos2).getBlock() != Blocks.BONE_BLOCK)
                return false;
        }

        return true;
    }

}
