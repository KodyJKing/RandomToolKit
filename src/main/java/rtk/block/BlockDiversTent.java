package rtk.block;

import net.minecraft.block.state.IBlockState;

public class BlockDiversTent extends BlockTentBreakable {
    public BlockDiversTent(String name) {
        super(name);
    }

    @Override
    public int fuelCost() {
        return 32;
    }

    @Override
    public IBlockState wall() {
        return ModBlocks.tentWall.variant(2);
    }

    @Override
    public boolean worksInWater() {
        return true;
    }
}
