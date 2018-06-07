package rtk.item;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.common.Common;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void init() {
        RTK.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        Common.formatToolTip(getUnlocalizedName() + ".tooltip", tooltip);
        if (GuiScreen.isShiftKeyDown())
            Common.formatToolTip(getUnlocalizedName() + ".tooltip2", tooltip);
    }
}
