package rtk.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderLightVoid extends WorldProviderSurface {

    public WorldProviderLightVoid() {
        super();
    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderVoid(worldObj, this.getSeed(), false, "");
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_){return new Vec3d(1, 1, 1);}

    public double getMovementFactor() { return 1.0 /8.0; }

    public float calculateCelestialAngle(long worldTime, float partialTicks){return 0.125F;}

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks){return null;}

    public boolean canRespawnHere() { return false; }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight(){return -10.0F;}

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {return new Vec3d(10, 10, 10);}

    @Override
    public boolean isDaytime() { return true; }

    @Override
    public float getStarBrightness(float par1) {return 10.0F;}
}