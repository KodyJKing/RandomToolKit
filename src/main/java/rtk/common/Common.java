package rtk.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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

    public static BlockPos blockLookedAt(EntityPlayer player, double range){
        World world = player.worldObj;

        if(!world.isRemote)
            return null;

        if(Minecraft.getMinecraft().objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
            return null;

        BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
        if(world.getBlockState(pos).getMaterial() == Material.AIR)
            return null;

        EnumFacing side = Minecraft.getMinecraft().objectMouseOver.sideHit;
        double dist = pos.distanceSqToCenter(player.posX, player.posY, player.posZ);
        if(dist > range * range)
            return null;

        return pos;
    }

    public static Vec3d randomVector(double length){
        Vec3d result = new Vec3d(Common.random.nextGaussian(), Common.random.nextGaussian(), Common.random.nextGaussian());
        result = result.normalize();
        result = result.scale(length);
        return result;
    }
}
