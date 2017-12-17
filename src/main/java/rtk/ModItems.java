package rtk;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.item.*;

public class ModItems {
    public static Item
            devTool, trowel, hotplate,
            hotplateEtched, dolly, barometer;

    @SubscribeEvent
    public void onItemRegistry(Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        devTool = register(new ItemDevTool("devtool"), registry);

        barometer = register(new ItemBarometer("barometer"), registry);
        trowel = register(new ItemTrowel("trowel"), registry);
        hotplate = register(new ItemHotplate("hotplate", false), registry);
        hotplateEtched = register(new ItemHotplate("hotplateetched", true), registry);
        dolly = register(new ItemDolly("dolly"), registry);
    }

    private static <T extends Item> T register(T item, IForgeRegistry<Item> registry) {
        registry.register(item);
        return item;
    }
}
