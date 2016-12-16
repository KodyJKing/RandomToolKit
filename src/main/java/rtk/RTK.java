package rtk;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = RTK.modId, name = RTK.name, version = RTK.version, acceptedMinecraftVersions = "[1.10, 1.10.2]")
public class RTK {

    public static final String modId = "rtk";
    public static final String name = "Random Tool Kit";
    public static final String version = "1.0.0";

    @SidedProxy(clientSide = "rtk.ClientProxy", serverSide = "rtk.CommonProxy")
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


        GameRegistry.addRecipe(new ItemStack(ModItems.needle, 4),
                "I##",
                "#I#",
                "###", 'I', Items.IRON_INGOT);

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

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.hotplateEtched), new Object[] {ModItems.hotplate, Blocks.STONEBRICK, Items.DIAMOND, Items.DIAMOND});
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

}
