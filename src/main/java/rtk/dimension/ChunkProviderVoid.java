package rtk.dimension;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.MapGenBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChunkProviderVoid extends ChunkProviderFlat {

    World worldObj;

    public ChunkProviderVoid(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings) {
        super(worldIn, seed, generateStructures, flatGeneratorSettings);
        worldObj = worldIn;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        Biome[] abiome = this.worldObj.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int l = 0; l < abyte.length; ++l)
        {
            abyte[l] = (byte)Biome.getIdForBiome(abiome[l]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {
//        super.populate(x, z);
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
//        return super.generateStructures(chunkIn, x, z);
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
//        return super.getPossibleCreatures(creatureType, pos);
        return new ArrayList<Biome.SpawnListEntry>();
    }

    @Nullable
    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
//        return super.getStrongholdGen(worldIn, structureName, position);
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
//        super.recreateStructures(chunkIn, x, z);
    }
}
