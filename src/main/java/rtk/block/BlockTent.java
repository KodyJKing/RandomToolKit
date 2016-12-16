package rtk.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.World;
import rtk.ModBlocks;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class BlockTent extends BlockBaseTent{
    public BlockTent(String name) {
        super(name);
    }

    public int fuelCost(){
        return 8;
    }

    public Item fuelType(){return Items.COAL;}

    @Override
    public IBlockState wall() {
        return ModBlocks.tentWall.getDefaultState();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if(world.isRemote)
            return;

        int h = width() - 1; //Height
        int r = h / 2; //Radius

        for(BlockPos pos2 : new BlockPos[] {pos.north(), pos.south(), pos.east(), pos.west(), pos.up(), pos.down()})
            BlockTentWall.tryPop(world, null, pos2.getX(), pos2.getY(), pos2.getZ(), true);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote)
            return true;
        if(canSpendFuel(player)){
            if(tryBuildTent(world, pos, player, side)){
                spendFuel(player);
            }
        }
        return true;
    }

    public boolean canSpendFuel(EntityPlayer player){
        if(player.capabilities.isCreativeMode)
            return true;
        InventoryPlayer inv = player.inventory;
        ItemStack cur = inv.getCurrentItem();
        if(cur != null && cur.getItem() == fuelType() && cur.stackSize >= fuelCost()){
            return true;
        }
        return false;
    }

    public void spendFuel(EntityPlayer player){
        if(player.capabilities.isCreativeMode)
            return;
        InventoryPlayer inv = player.inventory;
        ItemStack cur = inv.getCurrentItem();
        if(cur.getItem() == fuelType() && cur.stackSize >= fuelCost()){
            inv.decrStackSize(inv.currentItem, fuelCost());
        }
    }
}
