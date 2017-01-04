package rtk.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockUltraDispenser extends BlockBaseDirectional {
    public BlockUltraDispenser(String name) {
        super(Material.IRON, name);
        setCreativeTab(CreativeTabs.REDSTONE);
    }
}
