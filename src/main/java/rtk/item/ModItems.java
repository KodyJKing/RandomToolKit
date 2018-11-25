package rtk.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.RTK;
import rtk.block.BlockBase;
import rtk.item.*;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    private static List<Item> toRegister = new ArrayList<>();
    public static Item
            devTool, inspectionTool, trowel, hotplate,
            hotplateEtched, needle, dolly, barometer,
            toolbelt, earthStrider, earthStriderDrained,
            hotSword, netherPearl, eyeOfNether, toolbox,
            voidStrider, stopwatch;

    public void init() {
        if (RTK.devEnv)
            devTool = add(new ItemDevTool("devtool"));
        inspectionTool = add(new ItemInspectionTool("inspectiontool"));
        barometer = add(new ItemBarometer("barometer"));
        trowel = add(new ItemTrowel("trowel"));
        hotplate = add(new ItemHotplate("hotplate", false));
        hotplateEtched = add(new ItemHotplate("hotplateetched", true));
        needle = add(new ItemNeedle("needle"));
        dolly = add(new ItemDolly("dolly"));
        toolbelt = add(new ItemToolbelt("toolbelt"));
        earthStrider = add(new ItemEarthStrider("earthstrider"));
        earthStriderDrained = add(new ItemEarthStriderDrained("earthstrider_drained"));
        hotSword = add(new ItemHotSword("hotsword"));
        netherPearl = add(new ItemBase("netherpearl")).setMaxStackSize(16);
        eyeOfNether = add(new ItemEyeOfNether("eyeofnether"));
        toolbox = add(new ItemToolbox("toolbox"));
        voidStrider = add(new ItemVoidStrider("voidstrider"));
        stopwatch = add(new ItemStopwatch(("stopwatch")));
    }

    @SubscribeEvent
    public void onItemRegistry(Register<Item> event) {
        init();
//        System.out.println("RTK is registering items...");
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
        toRegister.clear();
    }

    public static <T extends Item> T add(T item) {
        toRegister.add(item);
        return item;
    }
}
