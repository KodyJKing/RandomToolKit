package rtk;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.block.BlockBase;
import rtk.item.*;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    private static List<Item> toRegister = new ArrayList<>();
    public static Item
            devTool, trowel, hotplate,
            hotplateEtched, dolly, barometer,
            toolbelt, earthStrider, earthStriderDrained;

    public ModItems() {
        devTool = add(new ItemDevTool("devtool"));
        barometer = add(new ItemBarometer("barometer"));
        trowel = add(new ItemTrowel("trowel"));
        hotplate = add(new ItemHotplate("hotplate", false));
        hotplateEtched = add(new ItemHotplate("hotplateetched", true));
        dolly = add(new ItemDolly("dolly"));
        toolbelt = add(new ItemToolbelt("toolbelt"));
        earthStrider = add(new ItemEarthStrider("earthstrider"));
        earthStriderDrained = add(new ItemBase("earthstrider_drained"));
    }

    @SubscribeEvent
    public void onItemRegistry(Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Item item: toRegister) {
            registry.register(item);
            if (item instanceof ItemBase) {
                ((ItemBase)item).init();
            } else if (item instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock)item;
                Block block = itemBlock.getBlock();
                if (block instanceof BlockBase)
                    ((BlockBase)block).init(itemBlock);
            }
        }
    }


    public static <T extends Item> T add(T item) {
        toRegister.add(item);
        return item;
    }
}
