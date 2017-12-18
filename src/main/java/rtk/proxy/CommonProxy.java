package rtk.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.Item;

public class CommonProxy {
    public void registerItemRenderer(Item item, int meta, String name){}
    public void ignoreProperty(Block block, IProperty property){}
}
