package rtk.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rtk.ModBlocks;
import rtk.common.CNBT;
import rtk.tileentity.TileEntityEnderTent;

import java.util.List;
import java.util.Random;

public class BlockEnderTent extends BlockTent {

    public BlockEnderTent(String name) {
        super(name);
    }

    @Override
    public int fuelCost() { return 16; }

    @Override
    public Item fuelType() { return Items.ENDER_PEARL; }

    @Override
    public IBlockState wall() {
        return ModBlocks.enderTentWall.getDefaultState();
    }

    @Override
    public boolean worksInWater() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityEnderTent();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityEnderTent.class;
    }

    public TileEntityEnderTent getTileEntity(IBlockAccess world, BlockPos pos){
        return (TileEntityEnderTent)world.getTileEntity(pos);
    }

    @Override
    public boolean tryBuildTent(World world, BlockPos pos, EntityPlayer player, EnumFacing side) {
        TileEntityEnderTent te = getTileEntity(world, pos);
        boolean result = false;
        if(te.isFirstDeploy())
            result = super.tryBuildTent(world, pos, player, side);
        else if(!te.isDeployed() && canBuildTent(world, pos)){
            placeContents(world, pos);
            result = true;
        }
        if(result){
            te.setDeployed(true);
            te.setFirstDeploy(false);
            world.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.5F, 1F);
        }
        return result;
    }

    @Override
    public void decorate(World world, BlockPos pos, EntityPlayer player, EnumFacing side) {
    }

    public void placeContents(World world, BlockPos pos) {

        TileEntityEnderTent te = getTileEntity(world, pos);
        if(te.isFirstDeploy())
            return;

        int h = width() - 1; //Height
        int r = h / 2; //Radius

        double p = 1; //AABB padding
        AxisAlignedBB tentBounds = new AxisAlignedBB(pos.getX() - r - p, pos.getY() - p, pos.getZ() - r - p, pos.getX() + r + p, pos.getY() + h + p, pos.getZ() + r + p);

        List<EntityItem> oldItems = world.getEntitiesWithinAABB(EntityItem.class, tentBounds);

        NBTTagList blockList = te.getBlockList();

        for(int attempt = 0; attempt < 2; attempt++){ int bsInd = 0;
        for(int y = pos.getY(); y <= pos.getY() + h; y++){
            for(int x = pos.getX() - r; x <= pos.getX() + r; x++){
                for(int z = pos.getZ() - r; z <= pos.getZ() + r; z++){
                    if(y != pos.getY() || x != pos.getX() || z != pos.getZ()){
                        CNBT.placeBlockFromNBT(world, new BlockPos(x, y, z), blockList.getCompoundTagAt(bsInd));
                    }
                    bsInd++;
                }
            }
        }}

        List<EntityItem> currentItems = world.getEntitiesWithinAABB(EntityItem.class, tentBounds);

        for(EntityItem item : currentItems){
            if(!oldItems.contains(item))
                item.setDead();
        }
    }

    public boolean tryGrabContents(World world, BlockPos pos){
        TileEntityEnderTent te = getTileEntity(world, pos);
        if(!te.isDeployed())
            return false;

        int h = width() - 1; //Height
        int r = h / 2; //Radius

        NBTTagList blockList = new NBTTagList();

        for(int y = pos.getY(); y <= pos.getY() + h; y++){
            for(int x = pos.getX() - r; x <= pos.getX() + r; x++){
                for(int z = pos.getZ() - r; z <= pos.getZ() + r; z++){
                    blockList.appendTag(CNBT.NBTFromBlock(world, new BlockPos(x, y, z)));
                }
            }
        }

        double p = 1; //AABB padding
        AxisAlignedBB tentBounds = new AxisAlignedBB(pos.getX() - r - p, pos.getY() - p, pos.getZ() - r - p, pos.getX() + r + p, pos.getY() + h + p, pos.getZ() + r + p);

        List<EntityItem> oldItems = world.getEntitiesWithinAABB(EntityItem.class, tentBounds);

        for(int z = pos.getZ() - r; z <= pos.getZ() + r; z++){
            for(int x = pos.getX() - r; x <= pos.getX() + r; x++){
                for(int y = pos.getY(); y <= pos.getY() + h; y++){
                    if(y != pos.getY() || x != pos.getX() || z != pos.getZ()){
                        //world.removeTileEntity(new BlockPos(x, y, z));
                        world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 2);
                    }
                }
            }
        }

        List<EntityItem> currentItems = world.getEntitiesWithinAABB(EntityItem.class, tentBounds);

        for(EntityItem item : currentItems){
            if(!oldItems.contains(item))
                item.setDead();
        }

        world.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.5F, 1F);

        te.setBlockList(blockList);
        te.setDeployed(false);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(stack.hasTagCompound())
            getTileEntity(world, pos).readFromNBT(stack.getTagCompound());
        EntityPlayer player = (EntityPlayer)placer;
        if(player != null && player.capabilities.isCreativeMode)
            player.inventory.deleteStack(stack);
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if(world.isRemote)
            return;

        tryGrabContents(world, pos);

        ItemStack drop = new ItemStack(ModBlocks.enderTent, 1);
        getTileEntity(world, pos).writeToNBT(CNBT.ensureCompound(drop));
        EntityItem item = new EntityItem(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, drop);
        world.spawnEntityInWorld(item);
    }
}
