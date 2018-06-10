package rtk.dimension;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorFlat;

import java.util.List;

public class ChunkGeneratorVoid extends ChunkGeneratorFlat {

    World world;

    public ChunkGeneratorVoid(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings) {
        super(worldIn, seed, generateStructures, flatGeneratorSettings);
        world = worldIn;
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        Biome[] abiome = this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int l = 0; l < abyte.length; ++l)
            abyte[l] = (byte)Biome.getIdForBiome(abiome[l]);

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType != EnumCreatureType.MONSTER)
            return Biome.getBiome(1).getSpawnableList(creatureType);
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
    }
}
