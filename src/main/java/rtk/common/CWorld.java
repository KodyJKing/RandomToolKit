package rtk.common;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class CWorld {
    public static boolean shouldReplace(World world, BlockPos pos){
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock().isReplaceable(world, pos) || !bs.isOpaqueCube() && bs.getBlockHardness(world, pos) < 0.01F || bs.getMaterial() == Material.AIR;
    }

    // Still avoids calls to breakBlock and onBlockAdded, but fixes lighting, elevation and does updates.
    // This is a modified version of World.setBlockState.
    public static void silentSetBlockStateAndUpdate(World world, BlockPos pos, IBlockState newState, int flags){
        if (world.isOutsideBuildHeight(pos)) {
            return;
        } else {
            Chunk chunk = world.getChunkFromBlockCoords(pos);

            pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
            net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;
            if (world.captureBlockSnapshots && !world.isRemote)
            {
                blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(world, pos, flags);
                world.capturedBlockSnapshots.add(blockSnapshot);
            }
            IBlockState oldState = world.getBlockState(pos);
            int oldLight = oldState.getLightValue(world, pos);
            int oldOpacity = oldState.getLightOpacity(world, pos);

            IBlockState iblockstate = chunk.getBlockState(pos);
            silentSetBlockState(world, pos, newState); // RandomToolKit - silently set the block state before calling into chunk.setBlockState so breakBlock and onBlockAdded won't get called.
            chunk.setBlockState(pos, newState);

            if (iblockstate == null)
            {
                if (blockSnapshot != null) world.capturedBlockSnapshots.remove(blockSnapshot);
                return;
            }
            else
            {
                if (newState.getLightOpacity(world, pos) != oldOpacity || newState.getLightValue(world, pos) != oldLight)
                {
                    world.profiler.startSection("checkLight");
                    world.checkLight(pos);
                    world.profiler.endSection();
                }

                if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    world.markAndNotifyBlock(pos, chunk, iblockstate, newState, flags);
                }
            }
        }
    }

    // To Avoid calls to breakBlock and onBlockAdded.
    // Also avoids updating lighting, and elevation information and updates.
    // See Chunk.setBlockState.
    public static void silentSetBlockState(World world, BlockPos pos, IBlockState state){
        world.removeTileEntity(pos);

        int dx = pos.getX() & 15;
        int dz = pos.getZ() & 15;
        int y = pos.getY();

        Chunk chunk = world.getChunkFromBlockCoords(pos);

        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];

        if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE)
            return;

        extendedblockstorage.set(dx, y & 15, dz, state);
        chunk.setModified(true);
    }

    public BlockPos pickSpawnPoint(World world, BlockPos pos, int radius, int tries, int scanHeight){
        for (int i = 0; i < tries; i++){
            BlockPos scanPos = pos.add(new BlockPos(CMath.randomVector(radius)));

            for (int j = 0; j < scanHeight; j++){
                if (world.isSideSolid(scanPos.down(), EnumFacing.UP) && world.isAirBlock(scanPos) && world.isAirBlock(scanPos.up()))
                    return scanPos;
                scanPos = scanPos.up();
            }
        }

        return null;
    }

}
