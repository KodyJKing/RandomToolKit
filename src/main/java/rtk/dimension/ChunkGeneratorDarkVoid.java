package rtk.dimension;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import rtk.common.CMath;

import java.util.List;

public class ChunkGeneratorDarkVoid extends ChunkGeneratorVoid {

    public ChunkGeneratorDarkVoid(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings) {
        super(worldIn, seed, generateStructures, flatGeneratorSettings);
    }

//    @Override
//    public Chunk generateChunk(int x, int z) {
//        Chunk chunk = super.generateChunk(x, z);
//        for (BlockPos pos: CMath.cuboid(new BlockPos(0, 255, 0), new BlockPos(15, 255, 15)))
//            chunk.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
//        return chunk;
//    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER)
            return Biome.getBiome(1).getSpawnableList(creatureType);
        return null;
    }
}