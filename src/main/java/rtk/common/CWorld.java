package rtk.common;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import javax.annotation.Nullable;
import java.util.List;

public class CWorld {
    public static boolean shouldReplace(World world, BlockPos pos) {
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock().isReplaceable(world, pos) || !bs.isOpaqueCube() && bs.getBlockHardness(world, pos) < 0.01F || bs.getMaterial() == Material.AIR;
    }

    // Still avoids calls to breakBlock and onBlockAdded, but fixes lighting, elevation and does updates.
    // This is a modified version of World.setBlockState.
    public static void silentSetBlockStateAndUpdate(World world, BlockPos pos, IBlockState newState, int flags) {
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
    public static void silentSetBlockState(World world, BlockPos pos, IBlockState state) {
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

    public static RayTraceResult getMouseover(EntityLivingBase entity, double range) {
        Vec3d eye = new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);

        RayTraceResult blocks = traceBlocks(entity, range);
        RayTraceResult entities = traceEntities(entity, range);

        if (blocks == null || blocks.typeOfHit == RayTraceResult.Type.MISS)
            return entities;
        if (entities == null || entities.typeOfHit == RayTraceResult.Type.MISS)
            return blocks;

        if (blocks.hitVec.squareDistanceTo(eye) < entities.hitVec.squareDistanceTo(eye))
            return blocks;
        return entities;
    }

    public static RayTraceResult traceBlocks(Entity entity, double range) {
        Vec3d eye = entity.getPositionEyes(1);
        Vec3d look = entity.getLookVec();
        Vec3d endPoint = eye.add(look.scale(range));
        return entity.world.rayTraceBlocks(eye, endPoint, false, false, true);
    }

    public static RayTraceResult traceEntities(Entity entity, double range) {
        Vec3d look = entity.getLookVec();
        Vec3d eye = entity.getPositionEyes(1);
        Vec3d end = eye.add(look.scale(range));

        List<Entity> list = entity.world.getEntitiesInAABBexcluding(
                entity,
                entity.getEntityBoundingBox().expand(look.x * range, look.y * range, look.z * range).grow(1.0D, 1.0D, 1.0D),
                Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
                {
                    public boolean apply(@Nullable Entity other)
                    {
                        return other != null && other.canBeCollidedWith();
                    }
                }));

        RayTraceResult closest = null;
        double closeDistSq = Double.MAX_VALUE;
        for (Entity other: list) {

            if (other.getEntityBoundingBox().contains(eye))
                return new RayTraceResult(other);

            RayTraceResult hit = other.getEntityBoundingBox().calculateIntercept(eye, end);
            if (hit == null)
                continue;

            double distSq = hit.hitVec.squareDistanceTo(eye);
            if (distSq < closeDistSq && distSq < range * range) {
                closeDistSq = distSq;
                closest = hit;
                hit.typeOfHit = RayTraceResult.Type.ENTITY;
                hit.entityHit = other;
            }
        }

        return closest;
    }

    public BlockPos pickSpawnPoint(World world, BlockPos pos, int radius, int tries, int scanHeight) {
        for (int i = 0; i < tries; i++) {
            BlockPos scanPos = pos.add(new BlockPos(CMath.randomVector(radius)));

            for (int j = 0; j < scanHeight; j++) {
                if (world.isSideSolid(scanPos.down(), EnumFacing.UP) && world.isAirBlock(scanPos) && world.isAirBlock(scanPos.up()))
                    return scanPos;
                scanPos = scanPos.up();
            }
        }

        return null;
    }

}
