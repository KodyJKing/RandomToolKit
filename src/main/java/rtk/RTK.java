package rtk;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rtk.gui.RTKGuiHandler;
import rtk.proxy.CommonProxy;

@Mod(modid = RTK.modId, name = RTK.name, version = RTK.version, acceptedMinecraftVersions = "[1.10, 1.10.2]")
public class RTK {

    public static final String modId = "rtk";
    public static final String name = "Random Tool Kit";
    public static final String version = "1.0.0";

    @SidedProxy(clientSide = "rtk.proxy.ClientProxy", serverSide = "rtk.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(modId)
    public static RTK instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(name + " is loading!");
        ModItems.init();
        ModBlocks.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Building recipes for " + name + ".");

        GameRegistry.addRecipe(new ItemStack(ModBlocks.emergencyTent),
                "OLO",
                "LCL",
                "OLO", 'O', new ItemStack(Items.DYE, 1, 14), 'L', Items.LEATHER, 'C', new ItemStack(Items.COAL, 1, 1));

        GameRegistry.addRecipe(new ItemStack(ModBlocks.tent),
                "ILI",
                "LEL",
                "IFI", 'O', new ItemStack(Items.DYE, 1, 14), 'L', Items.LEATHER, 'E', ModBlocks.emergencyTent, 'I', Items.IRON_INGOT, 'F', Blocks.FURNACE);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.enderTent, 1),
                "BEB",
                "EIE",
                "BEB", 'E', Items.ENDER_EYE, 'B', Items.BLAZE_ROD, 'I', ModBlocks.tent);


        GameRegistry.addRecipe(new ItemStack(ModItems.trowel, 1),
                "II#",
                "IS#",
                "##S", 'I', Items.IRON_INGOT, 'S', Items.STICK);

        GameRegistry.addRecipe(new ItemStack(ModItems.hotplate, 1),
                "III",
                "III",
                "#S#", 'I', Items.IRON_INGOT, 'S', Items.STICK);

        GameRegistry.addRecipe(new ItemStack(ModItems.toolBelt, 1),
                "LLL",
                "L#L",
                "LIL", 'L', Items.LEATHER, 'I', Items.IRON_INGOT);

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.hotplateEtched), new Object[] {ModItems.hotplate, Blocks.STONEBRICK, Items.DIAMOND, Items.DIAMOND});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.needle), new Object[] {Items.IRON_INGOT, Items.STRING});

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new RTKGuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new RTKEvents());
    }

}
