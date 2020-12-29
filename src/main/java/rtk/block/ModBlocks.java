package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    private static List<Block> toRegister = new ArrayList<>();

    public static Block tent;

    public static void init() {
        tent = add(new BlockTent(), "tent");
    }

    public static <T extends Block> T add(T block, String registryName) {
        block.setRegistryName(registryName);
        toRegister.add(block);
        if (block instanceof IRTKBlock) {
            Item.Properties properties = ((IRTKBlock) block).itemProperties();
            if (properties != null) {
                Item item = new BlockItem(block, properties);
                ModItems.add(item, registryName);
            }
        }
        return block;
    }

    public static void register(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Block block: toRegister)
            registry.register(block);
        toRegister = null;
    }

}
