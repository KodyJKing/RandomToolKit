package rtk.common;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class CWorld {
    public static boolean shouldReplace(World world, BlockPos pos){
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock().isReplaceable(world, pos) || !bs.isOpaqueCube() && bs.getBlockHardness(world, pos) < 0.01F || bs.getMaterial() == Material.AIR;
    }

    public static void safeReplaceBlock(World world, BlockPos pos, IBlockState state, int flags){
        safeRemoveBlock(world, pos);
        world.setBlockState(pos, state, flags);

    }

    public static void safeRemoveBlock(World world, BlockPos pos){
        if(world.getBlockState(pos).getBlock() == Blocks.AIR)
            return;

        world.removeTileEntity(pos);

        int dx = pos.getX() & 15;
        int dz = pos.getZ() & 15;
        int y = pos.getY();

        Chunk chunk = world.getChunkFromBlockCoords(pos);

        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];

        if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE)
            return;

        extendedblockstorage.set(dx, y & 15, dz, Blocks.AIR.getDefaultState());
        chunk.setModified(true);
    }
}
