package rtk.common;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class CMath {
    public static Random random = new Random();

    public static Vec3d randomVector(){
        return new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
    }

    public static Vec3d randomVector(double length){
        return randomVector().normalize().scale(length);
    }

    public static Vec3d randomVector(double length, double angleSpread, Vec3d heading){
        if(Math.abs(angleSpread) < Math.PI / 180)
            return heading;

        double minDot = Math.cos(angleSpread) * length * heading.lengthVector();
        for(int i = 0; i < 100; i++){
            Vec3d result = randomVector(length);
            if(result.dotProduct(heading) >= minDot)
                return result;
        }

        return heading; //If we didn't hit it after 50 tries the target must be small so using the heading is fair.
    }

    public static Vec3d randomVector(Vec3d heading, double length, double inaccuracy){
        return heading.normalize().add(randomVector().scale(0.007499999832361937D * inaccuracy)).normalize().scale(length);
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

    public static Vec3d lerp(Vec3d start, Vec3d end, double amount){
        return start.add(end.subtract(start).scale(amount));
    }
}
