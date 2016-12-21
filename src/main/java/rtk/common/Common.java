package rtk.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
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

    public static Vec3d randomVector(double length){
        Vec3d result = new Vec3d(Common.random.nextGaussian(), Common.random.nextGaussian(), Common.random.nextGaussian());
        result = result.normalize();
        result = result.scale(length);
        return result;
    }

    public static int findExactStack(IInventory inventory, ItemStack stack){
        for(int i = 0; i < inventory.getSizeInventory(); i++){
            if(inventory.getStackInSlot(i) == stack)
                return i;
        }
        return -1;
    }

    public static BlockPos[] cuboid(BlockPos a, BlockPos b){
        int xMin = Math.min(a.getX(), b.getX());
        int yMin = Math.min(a.getY(), b.getY());
        int zMin = Math.min(a.getZ(), b.getZ());

        int xMax = Math.max(a.getX(), b.getX());
        int yMax = Math.max(a.getY(), b.getY());
        int zMax = Math.max(a.getZ(), b.getZ());


        BlockPos[] positions = new BlockPos[(1 + xMax - xMin) * (1 + yMax - yMin) * (1 + zMax - zMin)];
        int i = 0;

        for(int x = xMin; x <= xMax; x++){
            for(int y = yMin; y <= yMax; y++){
                for(int z = zMin; z <= zMax; z++){
                    positions[i++] = new BlockPos(x, y, z);
                }
            }
        }

        return positions;
    }
}
