package rtk.item;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import rtk.RTK;
import rtk.common.Common;

import java.util.List;

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

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        Common.formatToolTip(getUnlocalizedName() + ".tooltip", tooltip);
        if(GuiScreen.isShiftKeyDown())
            Common.formatToolTip(getUnlocalizedName() + ".tooltip2", tooltip);
    }
}
