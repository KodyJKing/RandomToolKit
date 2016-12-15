package rtk.common;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

public class CWorld {
    public static void setBlock(World world, int x, int y, int z, Block block){
        world.setBlockState(new BlockPos(x, y, z), block.getDefaultState(), 3);
    }
}
