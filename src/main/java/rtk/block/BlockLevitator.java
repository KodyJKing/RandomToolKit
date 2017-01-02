package rtk.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.tileentity.TileEntityLevitator;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockLevitator extends BlockBase {

    public static final PropertyDirection FACING = BlockDirectional.FACING;

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
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityLevitator();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityLevitator.class;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
        return this.getDefaultState().withProperty(FACING, facing);
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
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
