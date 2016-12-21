package rtk.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import rtk.RTK;

public class BlockTentBreakable extends BlockTent {
    public static final PropertyBool DEPLOYED = PropertyBool.create("deployed");

    public BlockTentBreakable(String name) {
        super(name);
        setDefaultState(this.blockState.getBaseState().withProperty(DEPLOYED, false));
    }

    @Override
    public void init(ItemBlock item) {
        super.init(item);
        RTK.proxy.setCustomStateMap(this, (new StateMap.Builder()).ignore(DEPLOYED).build());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DEPLOYED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DEPLOYED) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{DEPLOYED});
    }

    @Override
    public boolean tryBuildTent(World world, BlockPos pos, EntityPlayer player, EnumFacing side) {

        if(super.tryBuildTent(world, pos, player, side)){
            world.setBlockState(pos, world.getBlockState(pos).withProperty(DEPLOYED, true));
            return true;
        }

        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        super.onBlockDestroyedByPlayer(world, pos, state);

        if(!state.getValue(DEPLOYED))
            return;

        for(BlockPos otherPos : tentCuboid(pos))
            world.destroyBlock(otherPos, true);
    }
}
