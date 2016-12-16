package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import rtk.RTK;
import rtk.common.Common;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class BlockBase extends Block {

    protected String name;

    public BlockBase(Material material, String name){
        super(material);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel(ItemBlock item){
        RTK.proxy.registerItemRenderer(item, 0 , name);
    }

    public Class<? extends TileEntity> getTileEntityClass(){
        return null;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getTileEntityClass() != null;
    }

    public ItemBlock createItemBlock(Block block){
        return new ItemBlock(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        Common.formatToolTip(getUnlocalizedName() + ".tooltip", tooltip);
        if(GuiScreen.isShiftKeyDown())
            Common.formatToolTip(getUnlocalizedName() + ".tooltip2", tooltip);
    }
}
