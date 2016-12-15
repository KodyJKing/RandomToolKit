package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import rtk.RTK;

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
}
