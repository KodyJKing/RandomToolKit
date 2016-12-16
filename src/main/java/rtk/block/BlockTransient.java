package rtk.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTransient extends BlockBase {

    static final PropertyBool POWERED = PropertyBool.create("powered");

    static final AxisAlignedBB poweredBB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    static final AxisAlignedBB unPoweredBB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);


    public BlockTransient(String name) {
        super(Material.GLASS, name);
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return worldIn.isBlockPowered(pos) ? poweredBB : unPoweredBB;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }
}
