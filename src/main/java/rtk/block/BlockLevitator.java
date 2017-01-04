package rtk.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rtk.tileentity.TileLevitator;

import javax.annotation.Nullable;

public class BlockLevitator extends BlockBaseDirectional {

    public static final AxisAlignedBB DOWN_BOX = boundingBoxForDir(EnumFacing.DOWN);
    public static final AxisAlignedBB UP_BOX = boundingBoxForDir(EnumFacing.UP);
    public static final AxisAlignedBB NORTH_BOX = boundingBoxForDir(EnumFacing.NORTH);
    public static final AxisAlignedBB SOUTH_BOX = boundingBoxForDir(EnumFacing.SOUTH);
    public static final AxisAlignedBB WEST_BOX = boundingBoxForDir(EnumFacing.WEST);
    public static final AxisAlignedBB EAST_BOX = boundingBoxForDir(EnumFacing.EAST);

    public BlockLevitator(String name) {
        super(Material.GLASS, name);
        setCreativeTab(CreativeTabs.TRANSPORTATION);
        setTickRandomly(true);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLevitator();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileLevitator.class;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return getBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing dir = state.getValue(FACING);
        switch(dir){
            case DOWN:
                return DOWN_BOX;
            case UP:
                return UP_BOX;
            case NORTH:
                return NORTH_BOX;
            case SOUTH:
                return SOUTH_BOX;
            case WEST:
                return WEST_BOX;
            case EAST:
                return EAST_BOX;
        }
        return UP_BOX;
    }

    public static AxisAlignedBB boundingBoxForDir(EnumFacing dir){
        double x, y, z, x2, y2, z2;
        x = 0;
        y = 0;
        z = 0;
        x2 = 1;
        y2 = 1;
        z2 = 1;
        switch(dir){
            case DOWN:
                y += 0.5;
                break;
            case UP:
                y2 -= 0.5;
                break;
            case NORTH:
                z += 0.5;
                break;
            case SOUTH:
                z2 -= 0.5;
                break;
            case WEST:
                x += 0.5;
                break;
            case EAST:
                x2 -= 0.5;
                break;
        }
        return new AxisAlignedBB(x, y, z, x2, y2, z2);
    }
}
