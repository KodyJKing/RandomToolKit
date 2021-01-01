package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTentBreakable extends BlockTent {

    public static final BooleanProperty DEPLOYED = BooleanProperty.create("deployed");

    public BlockTentBreakable(Properties properties) {
        super(properties);
        setDefaultState(stateContainer.getBaseState().with(DEPLOYED,false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return stateContainer.getBaseState().with(DEPLOYED,false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DEPLOYED);
    }

    @Override
    public void onBuilt(World world, BlockPos pos, PlayerEntity player) {
        super.onBuilt(world, pos, player);
        world.setBlockState(pos, world.getBlockState(pos).with(DEPLOYED,true));
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, world, pos, newState, isMoving);
        if (state.get(DEPLOYED))
            cleanup(world, pos);
    }

    private void cleanup(World world, BlockPos pos) {
        int count = 0;
        for (BlockPos otherPos : tentCuboid(pos)) {
            BlockState bs = world.getBlockState(otherPos);
            Block block = bs.getBlock();
            if (block == wall.getBlock() || block == light.getBlock()) {
                world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 3);
                count++;
            }
        }
        if (count > 5)
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, .25f, .8f);
    }
}
