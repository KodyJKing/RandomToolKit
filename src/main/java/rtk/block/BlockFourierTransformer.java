package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.common.CMath;

import java.util.*;

public class BlockFourierTransformer extends BlockBaseDirectional {

    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

    public BlockFourierTransformer(String name) {
        super(Material.GLASS, name);
        setCreativeTab(CreativeTabs.REDSTONE);
        //setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, false));
        setDefaultState(this.getDefaultState().withProperty(TRIGGERED, false));
        setSoundType(SoundType.STONE);
    }

    @Override
    public void init(ItemBlock item) {
        super.init(item);
        RTK.proxy.ignoreProperty(this, TRIGGERED);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (world.isRemote)
            return;

        if (!state.getValue(TRIGGERED) && world.isBlockPowered(pos))
            moveEntities(world, pos, state.getValue(FACING));

        world.setBlockState(pos, state.withProperty(TRIGGERED, world.isBlockPowered(pos)));
    }

    public void moveEntities(World world, BlockPos pos, EnumFacing facing){
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.offset(facing), pos.offset(facing).south().east().up()));
        if (entities.isEmpty())
            return;

        BlockPos exit = findExit(world, pos);
        if (exit == null)
            return;

        IBlockState otherTransformer = world.getBlockState(exit);
        EnumFacing dir = otherTransformer.getValue(FACING);

        Vec3d destination = new Vec3d(exit).addVector(0.5, 0.5, 0.5).addVector(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());

        for (Entity e : entities){
            if (e instanceof EntityPlayerMP)
                ((EntityPlayerMP) e).connection.setPlayerLocation(destination.x, destination.y, destination.z, e.rotationYaw, e.rotationPitch);
            else
                e.setLocationAndAngles(destination.x, destination.y, destination.z, e.rotationYaw, e.rotationPitch);
        }

        world.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.5F, 1F);
        world.playSound(null, destination.x, destination.y, destination.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.5F, 1F);
    }

    //Breadth first search for a suitable exit.
    public BlockPos findExit(World world, BlockPos pos){
        Set<BlockPos> visited = new HashSet<BlockPos>();
        List<BlockPos> currentDepth = new ArrayList<BlockPos>();
        List<BlockPos> nextDepth = new ArrayList<BlockPos>();

        currentDepth.add(pos);
        while(!currentDepth.isEmpty()){
            for (BlockPos node : currentDepth){
                visited.add(node);

                for (BlockPos neighbor : CMath.cuboid(node.add(-1, -1, -1), node.add(1, 1, 1))){
                    if (!isConnected(world, node, neighbor) || visited.contains(neighbor))
                        continue;

                    if (isExit(world, neighbor))
                        return neighbor;

                    if (isWire(world, neighbor))
                        nextDepth.add(neighbor);
                }
            }

            List<BlockPos> temp = currentDepth;
            currentDepth = nextDepth;
            nextDepth = temp;
            nextDepth.clear();
        }

        return null;
    }

    public boolean isExit(World world, BlockPos pos){
        return world.getBlockState(pos).getBlock() instanceof BlockFourierTransformer && world.isBlockPowered(pos);
    }

    public boolean isWire(World world, BlockPos pos){
        return world.getBlockState(pos).getBlock() instanceof BlockRedstoneWire;
    }

    public boolean isBlockedDiagonal(World world, BlockPos from, BlockPos to){
        BlockPos mixX, mixY, mixZ;
        mixX = new BlockPos(to.getX(), from.getY(), from.getZ());
        mixY = new BlockPos(from.getX(), to.getY(), from.getZ());
        mixZ = new BlockPos(from.getX(), from.getY(), to.getZ());
        return !world.isAirBlock(mixX) && !world.isAirBlock(mixY) && !world.isAirBlock(mixZ);
    }

    public boolean isConnected(World world, BlockPos from, BlockPos to){
        Vec3i diff = from.subtract(to);
        if (diff.getX() * diff.getZ() != 0)
            return false;

        return diff.getY() == 0 || !isBlockedDiagonal(world, from, to);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING, TRIGGERED});
    }
}
