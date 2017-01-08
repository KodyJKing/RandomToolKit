package rtk;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import rtk.common.UDispenseBehavior;
import rtk.common.UDispenseHandler;
import rtk.gui.ModGuiHandler;
import rtk.proxy.CommonProxy;

@Mod(modid = RTK.modId, name = RTK.name, version = RTK.version, acceptedMinecraftVersions = "[1.10, 1.10.2]")
public class RTK {

    public static final String modId = "rtk";
    public static final String name = "Random Tool Kit";
    public static final String version = "1.1.1";

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
        ModRecipes.buildRecipes();
        UDispenseBehavior.registerBehaviors();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ModEvents());
    }

}
