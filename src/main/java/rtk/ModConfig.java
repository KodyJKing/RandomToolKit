package rtk;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = RTK.modId)
public class ModConfig {
    @Config.LangKey("config.tentfuelcost")
    public static TentFuelCost tentFuelCost = new TentFuelCost();

    public static class TentFuelCost {
        @Config.LangKey("tile.tent.name")
        public int tent = 8;
        @Config.LangKey("tile.diverstent.name")
        public int diversTent = 32;
        @Config.LangKey("tile.endertent.name")
        public int enderTent = 16;
        @Config.LangKey("tile.diversendertent.name")
        public int diversEnderTent = 16;
    }

    @Config.LangKey("config.earthstriderwhitelist")
    @Config.Comment("The materials that you can see/walk through with an Earth Strider.")
    public static String[] earthStriderWhitelist = { "minecraft:stone" };

    @Mod.EventBusSubscriber(modid = RTK.modId)
    static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RTK.modId))
                ConfigManager.sync(RTK.modId, Config.Type.INSTANCE);
        }
    }
}
