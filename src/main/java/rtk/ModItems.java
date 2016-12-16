package rtk;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rtk.item.*;

public class ModItems {
    public static ItemBase trowel, hotplate, hotplateEtched, needle, toolbox, toolBelt;

    public static void init() {
        trowel = register(new ItemTrowel("trowel"));
        hotplate = register(new ItemHotplate("hotplate", false));
        hotplateEtched = register(new ItemHotplate("hotplateEtched", true));
        needle = register(new ItemNeedle("needle"));
        toolbox = register(new ItemToolbox("toolbox"));
        toolBelt = register(new ItemToolbelt("toolbelt"));
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof ItemBase) {
            ((ItemBase)item).registerItemModel();
        }

        return item;
    }
}
