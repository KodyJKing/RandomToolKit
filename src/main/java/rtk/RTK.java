package rtk;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rtk.proxy.CommonProxy;

@Mod(modid = RTK.modId, name = RTK.name, version = RTK.version, acceptedMinecraftVersions = "[1.12.2]")
public class RTK {

    public static final String modId = "rtk";
    public static final String name = "Random Tool Kit";
    public static final String version = "1.3";

    @SidedProxy(clientSide = "rtk.proxy.ClientProxy", serverSide = "rtk.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(modId)
    public static RTK instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(name + " is loading!");
        MinecraftForge.EVENT_BUS.register(new ModItems());
    }

}
