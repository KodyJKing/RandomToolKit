package rtk.dimension;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class WorldProviderLightVoid extends WorldProviderSurface {

    public WorldProviderLightVoid() {
        super();
    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorVoid(world, this.getSeed(), false, "");
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {return new Vec3d(1, 1, 1);}

    public double getMovementFactor() { return 1.0 / 8.0; }

    public float calculateCelestialAngle(long worldTime, float partialTicks) {return 0.125F;}

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {return null;}

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {return -10.0F;}

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {return new Vec3d(10, 10, 10);}

    @Override
    public boolean isDaytime() { return true; }

    @Override
    public boolean canRespawnHere() { return true; }

    @Override
    public float getStarBrightness(float par1) {return 10.0F;}

    @Override
    public void updateWeather() {}
}
