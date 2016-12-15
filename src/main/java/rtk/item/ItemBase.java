package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import rtk.RTK;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel() {
        RTK.proxy.registerItemRenderer(this, 0, name);
    }

}
