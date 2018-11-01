package rtk.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderDarkVoid extends WorldProviderSurface {

    public WorldProviderDarkVoid() {
        super();
    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorDarkVoid(world, this.getSeed(), false, "");
    }


    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
    {return new Vec3d(0, 0, 0);}

    public double getMovementFactor() { return 8.0; }

    public boolean getHasNoSky() { return true; }

    public float getSunBrightnessFactor(float par1) { return 0; }

    public float calculateCelestialAngle(long worldTime, float partialTicks) {return 0.375F;}
//    public float calculateCelestialAngle(long worldTime, float partialTicks) {return super.calculateCelestialAngle(worldTime * 16, partialTicks * 16);}


    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {return null;}

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {return -10.0F;}

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {return new Vec3d(0, 0, 0);}

    @Override
    public boolean isDaytime() { return false; }

    @Override
    public boolean canRespawnHere() { return false; }

    @Override
    public float getStarBrightness(float par1) {return 0.75F;}

    @Override
    public void updateWeather() {}

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) { return false; }
}
