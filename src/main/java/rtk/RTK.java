package rtk;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import rtk.entity.EntityEyeOfNether;
import rtk.entity.EntitySkeletonPriest;
import rtk.gui.ModGuiHandler;
import rtk.item.ItemDevTool;
import rtk.proxy.CommonProxy;

@Mod(modid = RTK.modId, name = RTK.name, version = RTK.version, acceptedMinecraftVersions = "[1.12.2]", dependencies = "after:baubles;")
public class RTK {

    public static final String modId = "rtk";
    public static final String name = "Random Tool Kit";
    public static final String version = "1.3.4";

    @SidedProxy(clientSide = "rtk.proxy.ClientProxy", serverSide = "rtk.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(modId)
    public static RTK instance;

    public static boolean devEnv = false;

    public static boolean baublesLoaded = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(name + " is loading!");

        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
            devEnv = true;

        baublesLoaded = Loader.isModLoaded("baubles");

        MinecraftForge.EVENT_BUS.register(new ModBlocks());
        MinecraftForge.EVENT_BUS.register(new ModItems());
        MinecraftForge.EVENT_BUS.register(new ModRecipes());
        proxy.preInit();

        // TODO: Register these entities the new way https://mcforge.readthedocs.io/en/latest/concepts/registries/
        EntityRegistry.registerModEntity(
                new ResourceLocation(modId, "eyeofnether"),
                EntityEyeOfNether.class, modId + ".eyeofnether",
                2, this, 80, 1,
                true);
        EntityRegistry.registerModEntity(
                new ResourceLocation(modId, "skeletonpriest"),
                EntitySkeletonPriest.class, modId + ".skeletonpriest",
                3, this, 80, 1,
                true, 0xf2f2f2, 0x330000);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModDimensions.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ModEvents());
    }
}
