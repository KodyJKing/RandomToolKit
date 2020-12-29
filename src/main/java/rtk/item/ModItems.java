package rtk.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    private static List<Item> toRegister = new ArrayList<>();

    public static Item devtool, dolly;

    public static void init() {
        devtool = add("devtool", new ItemDevTool());
        dolly = add("dolly", new ItemDolly());
    }

    public static <T extends Item> T add(String registryName, T item) {
        item.setRegistryName(registryName);
        toRegister.add(item);
        return item;
    }

    public static void register(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Item item: toRegister)
            registry.register(item);
        toRegister = null;
    }

}
