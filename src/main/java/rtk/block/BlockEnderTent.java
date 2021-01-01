package rtk.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import rtk.common.CNBT;
import rtk.common.CWorld;
import rtk.tileentity.TileEnderTent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlockEnderTent extends BlockTent {

    {
        fuelType = Items.ENDER_PEARL;
    }

    public BlockEnderTent(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEnderTent();
    }

    public TileEnderTent getTileEntity(IWorld world, BlockPos pos) {
        return (TileEnderTent)world.getTileEntity(pos);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEnderTent te = getTileEntity(world, pos);
        boolean result = false;

        if (te.neverDeployed())
            result = tryBuildTent(world, pos, player);
        else {
            if (te.isDeployed()) {
                if (player.isSneaking()) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    result = true;
                }
            } else if (canBuildTent(world, pos, player)) {
                spendFuel(player);
                placeContents(world, pos);
                result = true;
            }
        }

        if (result) {
            te.setDeployed(true);
            te.setNeverDeployed(false);
            world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS.BLOCKS, 0.5F, 1F);
        }
        return result ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }

    @Override
    public void onBuilt(World world, BlockPos pos, PlayerEntity player) {}

    public void placeContents(World world, BlockPos pos) {
        TileEnderTent te = getTileEntity(world, pos);
        if (te.neverDeployed())
            return;

        BlockPos[] tentCube = tentCuboid(pos);

//        First set everything to dirt so nothing is without solid support during the building loop.
        for (BlockPos otherPos : tentCube) {
            if (pos.equals(otherPos))
                continue;  //Don't mess with the tent block!!!

            TileEntity otherTileEntity = world.getTileEntity(otherPos);
            if (otherTileEntity instanceof TileEnderTent) //We don't want another ender tent collapsing in a collapsing ender tent!!!
                ((TileEnderTent) otherTileEntity).dontGrab = true;

            world.setBlockState(otherPos, Blocks.DIRT.getDefaultState(), 2);
        }

        ListNBT blockList = te.getBlockList();

        int bsInd = 0;
        for (BlockPos otherPos : tentCube) {
            if (!pos.equals(otherPos))
                CNBT.placeBlockFromNBT(world, otherPos, blockList.getCompound(bsInd));
            bsInd++;
        }

        placeEntities(world, pos, te);

    }

    public boolean tryGrabContents(World world, BlockPos pos) {
        TileEnderTent te = getTileEntity(world, pos);
        if (!te.isDeployed())
            return false;

        BlockPos[] tentCube = tentCuboid(pos);

        ListNBT blockList = new ListNBT();

        for (BlockPos otherPos : tentCube)
            blockList.add(CNBT.NBTFromBlock(world, otherPos));

        //Next set everything to a solid block so nothing loses support during deletion. (I'm talking about you torches!)
        for (BlockPos otherPos : tentCube) {
            if (pos.equals(otherPos))
                continue;

            TileEntity otherTileEntity = world.getTileEntity(otherPos);
            if (otherTileEntity instanceof TileEnderTent) //We don't want another ender tent collapsing in a collapsing ender tent!!!
                ((TileEnderTent)otherTileEntity).dontGrab = true;

            CWorld.silentSetBlockState(world, otherPos, Blocks.DIRT.getDefaultState());
        }

        //Finally we can actually delete everything.
        for (BlockPos otherPos : tentCube) {
            if (!pos.equals(otherPos)) //Don't mess with the tent block!!!
                world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 3);
        }

        world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5F, 1F);

        if (!world.isRemote) {
            ListNBT entityList = grabEntities(world, pos);
            te.setEntityList(entityList);
            te.setBlockList(blockList);
            te.setDeployed(false);
            te.setLastPosition(pos);
        }

        return true;
    }

    public ListNBT grabEntities(World world, BlockPos pos) {
        int h = width - 1; //Height
        int r = h / 2; //Radius
        AxisAlignedBB aabb = new AxisAlignedBB(pos.add(r, h, r), pos.add(-r, 0, -r));
        List<Entity> entities = world.getEntitiesInAABBexcluding(null, aabb, entity -> true);
        ListNBT entityList = new ListNBT();
        for (Entity e: entities) {
            if (e instanceof PlayerEntity)
                continue;
            if (!world.isRemote) {
                ServerWorld world1 = (ServerWorld) world;
                if (world1 != null) {
                    if (e instanceof ContainerMinecartEntity)
                        ((ContainerMinecartEntity)e).dropContentsWhenDead(false);
                    world1.removeEntityComplete(e, false);
                } else {
                    continue;
                }
            }
            CompoundNBT entityNBT = e.serializeNBT();
            entityList.add(entityNBT);
        }
        return entityList;
    }

    public void placeEntities(World world, BlockPos pos, TileEnderTent te) {
        if (world.isRemote)
            return;
        ListNBT entityList = te.getEntityList();
        Vector3i diff = pos.subtract(te.getLastPosition());
        Vector3d diffD = new Vector3d(diff.getX(), diff.getY(), diff.getZ());
        for (INBT inbt: entityList) {
            CompoundNBT nbt = (CompoundNBT) inbt;
            if (nbt == null) continue;
            Optional<Entity> copy = EntityType.loadEntityUnchecked(nbt, world);
            if (copy.isPresent()) {
                Entity e = copy.get();
                e.moveForced(e.getPositionVec().add(diffD));
                world.addEntity(e);
            }
        }
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasTag())
            getTileEntity(world, pos).readTent(stack.getTag());

        PlayerEntity player = (PlayerEntity) placer;
        if (player != null && player.isCreative() && stack.getCount() == 1)
            player.inventory.deleteStack(stack);

        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

        TileEnderTent te = getTileEntity(world, pos);
        if (te.dontGrab)
            return;

        ItemStack drop = new ItemStack(this, 1);
        tryGrabContents(world, pos);
        if (!te.isDeployed() && !te.neverDeployed())
            drop.addEnchantment(Enchantments.INFINITY, 1); //This is just an indicator that the tent is full.

        getTileEntity(world, pos).writeTent(drop.getOrCreateTag());
        ItemEntity item = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, drop);
        world.addEntity(item);

    }

}
