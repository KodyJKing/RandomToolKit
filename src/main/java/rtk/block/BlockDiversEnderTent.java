package rtk.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import rtk.ModBlocks;

public class BlockDiversEnderTent extends BlockEnderTent {
    public BlockDiversEnderTent(String name) {
        super(name);
    }

    @Override
    public boolean worksInWater() {
        return true;
    }

    @Override
    public IBlockState wall() {
        return ModBlocks.tentWall.variant(5);
    }

    @Override
    public ItemStack tentDrop() {
        return new ItemStack(ModBlocks.diversEnderTent, 1);
    }
}
