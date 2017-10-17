package rtk;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rtk.item.*;

public class ModItems {
    public static Item
            devTool, trowel, hotplate,
            hotplateEtched, needle, toolbox,
            toolbelt, dolly, earthStrider,
            earthStriderDrained, barometer,
            hotSword, netherPearl, eyeOfNether;

    public static void init() {
        devTool = register(new ItemDevTool("devTool"));

        trowel = register(new ItemTrowel("trowel"));
        hotplate = register(new ItemHotplate("hotplate", false));
        hotplateEtched = register(new ItemHotplate("hotplateEtched", true));
        needle = register(new ItemNeedle("needle"));
        toolbox = register(new ItemToolbox("toolbox"));
        toolbelt = register(new ItemToolbelt("toolbelt"));
        dolly = register(new ItemDolly("dolly"));
        earthStrider = register(new ItemEarthStrider("earthStrider"));
        earthStriderDrained = register(new ItemBase("earthStrider_drained"));
        barometer = register(new ItemBarometer("barometer"));
        hotSword = register(new ItemHotSword("hotSword"));
        netherPearl = register(new ItemBase("netherPearl")).setMaxStackSize(16);
        eyeOfNether = register(new ItemEyeOfNether("eyeOfNether"));
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof ItemBase) {
            ((ItemBase)item).init();
        }

        return item;
    }
}
