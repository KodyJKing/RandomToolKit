package rtk.dimension;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class ChunkGeneratorDarkVoid extends ChunkGeneratorVoid {

    public ChunkGeneratorDarkVoid(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings) {
        super(worldIn, seed, generateStructures, flatGeneratorSettings);
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER)
            return Biome.getBiome(1).getSpawnableList(creatureType);
        return null;
    }
}