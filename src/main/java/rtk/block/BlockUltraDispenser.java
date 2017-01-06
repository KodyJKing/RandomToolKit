package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.common.UltraDispenserHandler;
import rtk.item.ItemUltraDispenser;
import rtk.tileentity.TileUltraDispenser;

import javax.annotation.Nullable;

public class BlockUltraDispenser extends BlockBaseDirectional {
    public BlockUltraDispenser(String name) {
        super(Material.IRON, name);
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileUltraDispenser.class;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileUltraDispenser();
    }

    @Override
    public ItemBlock createItemBlock(Block block) {
        return new ItemUltraDispenser(block);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(player.isSneaking())
            System.out.println(((TileUltraDispenser)world.getTileEntity(pos)).data);
        else
            openGui(player, pos, world);
        return true;
    }

    public void openGui(EntityPlayer player, BlockPos pos, World world){
        if(!world.isRemote)
            player.openGui(RTK.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
    }
}
