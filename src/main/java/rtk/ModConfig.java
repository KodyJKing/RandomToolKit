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

    @Config.LangKey("config.dimension")
    public static Dimension dimension = new Dimension();
    public static class Dimension {
        @Config.Comment("Leave as 0 to auto assign id.")
        @Config.LangKey("config.dimension.darkvoidid")
        public int darkvoidid = 0;
        @Config.Comment("Leave as 0 to auto assign id.")
        @Config.LangKey("config.dimension.lightvoidid")
        public int lightvoidid = 0;
    }

    @Config.LangKey("config.earthstriderwhitelist")
    @Config.Comment("The materials that you can see/walk through with an Earth Strider.")
    public static String[] earthStriderWhitelist = { "minecraft:stone" };

    @Config.LangKey("config.dollywhitelist")
    @Config.Comment("Additional blocks the dolly is allowed to pick up.")
    public static String[] dollyWhitelist = {
            "minecraft:obsidian",
            "minecraft:mob_spawner",
            "storagedrawers:basicdrawers",
            "storagedrawers:compdrawers",
            "storagedrawers:customdrawers",
            "actuallyadditions.block_giant_chest",
            "actuallyadditions.block_giant_chest_medium",
            "actuallyadditions.block_giant_chest_large"
    };

    @Config.LangKey("config.dollyblacklist")
    @Config.Comment("Blocks the dolly is not allowed to pick up.")
    public static String[] dollyBlacklist = {};

    @Mod.EventBusSubscriber(modid = RTK.modId)
    static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RTK.modId))
                ConfigManager.sync(RTK.modId, Config.Type.INSTANCE);
        }
    }
}
