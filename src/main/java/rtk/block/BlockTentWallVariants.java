package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import rtk.RTK;
import rtk.item.ItemBlockVariants;

import java.util.List;

public class BlockTentWallVariants extends BlockTentWall {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0 , 1);

    public BlockTentWallVariants(String name) {
        super(name);

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, 0));
    }

    @Override
    public void init(ItemBlock item) {
        //super.init(item);
        for(Integer i : VARIANT.getAllowedValues())
            RTK.proxy.registerItemRenderer(item, i, name + "_"  + i.toString());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public ItemBlock createItemBlock(Block block) {
        return new ItemBlockVariants(block);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for(Integer i : VARIANT.getAllowedValues())
            list.add(new ItemStack(itemIn, 1, i));
    }
}
