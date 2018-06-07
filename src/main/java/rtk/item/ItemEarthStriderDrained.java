package rtk.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class ItemEarthStriderDrained extends ItemBase implements IBauble {
    public ItemEarthStriderDrained(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }
}
