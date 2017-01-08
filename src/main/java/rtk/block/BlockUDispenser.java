package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.common.UDispenseHandler;
import rtk.item.ItemUDispenser;
import rtk.tileentity.TileUDispenser;

import javax.annotation.Nullable;

public class BlockUDispenser extends BlockBaseDirectional {
    public BlockUDispenser(String name) {
        super(Material.IRON, name);
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileUDispenser.class;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileUDispenser();
    }

    @Override
    public ItemBlock createItemBlock(Block block) {
        return new ItemUDispenser(block);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        openGui(player, pos, world);
        return true;
    }

    public void openGui(EntityPlayer player, BlockPos pos, World world){
        if(!world.isRemote)
            player.openGui(RTK.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        if(world.isRemote)
            return;

        TileUDispenser tile = (TileUDispenser) world.getTileEntity(pos);
        boolean powered = world.isBlockPowered(pos);
        if(powered && !tile.isPowered()){
            Vec3d dirVec = new Vec3d(state.getValue(FACING).getDirectionVec());
            Vec3d startVec = new Vec3d(pos).add(dirVec.scale(0.7)).addVector(0.5, 0.5, 0.5);
            UDispenseHandler.tryFire(world, startVec, dirVec, tile, null);
        }

        tile.setPowered(powered);
    }
}
