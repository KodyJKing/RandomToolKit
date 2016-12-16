package rtk.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.Random;

public class Common {
    public static Random random = new Random();

    public static void formatToolTip(String key, List<String> target){
        if(!I18n.hasKey(key))
            return;
        String[] lines = I18n.format(key).split(";");
        target.addAll(Arrays.asList(lines));
    }

    public static void setBlock(World world, int x, int y, int z, Block block){
        world.setBlockState(new BlockPos(x, y, z), block.getDefaultState(), 3);
    }

    public static boolean shouldReplace(World world, BlockPos pos){
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock().isReplaceable(world, pos) || !bs.isOpaqueCube() && bs.getBlockHardness(world, pos) < 0.01F || bs.getMaterial() == Material.AIR;
    }
}
