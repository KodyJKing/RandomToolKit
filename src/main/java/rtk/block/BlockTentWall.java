package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.common.CMath;
import rtk.item.ItemBlockVariants;

import java.util.Random;

public class BlockTentWall extends BlockBase {

    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0 , 5);

    public BlockTentWall(String name){
        super(Material.CLOTH, name);
        setSoundType(SoundType.CLOTH);
        setHardness(0.2F);
        setResistance(0.5F);

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, 0));
    }

    public static boolean isTentWall(World world, BlockPos pos){
        return world.getBlockState(pos).getBlock() instanceof BlockTentWall;
    }

    public static void tryPop(World world, BlockPos pos){
        if (isTentWall(world, pos)){

            world.setBlockToAir(pos);
            if (!world.isRemote && CMath.random.nextInt(32) == 0)
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 0.0F, false);

            for (BlockPos otherPos : CMath.cuboid(pos.add(-1, -1, -1), pos.add(1, 1, 1))){
                tryPop(world, otherPos);
            }
        }
    }

    @Override
    public void init(ItemBlock item) {
        for (Integer i : VARIANT.getAllowedValues())
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
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (Integer i : VARIANT.getAllowedValues())
            items.add(new ItemStack(this, 1, i));
    }

    public IBlockState variant(int i){
        return getDefaultState().withProperty(VARIANT, i);
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(VARIANT) == 4 ? 15 : 0;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 0;
    }
}
