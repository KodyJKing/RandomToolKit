package rtk;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import rtk.dimension.TeleporterRTK;
import rtk.dimension.WorldProviderDarkVoid;
import rtk.dimension.WorldProviderLightVoid;

public class ModDimensions {

    public static int darkVoidId, lightVoidId;
    public static DimensionType darkVoidType, lightVoidType;

    public static TeleporterRTK teleporterDark, teleporterLight;

    public static void init() {
        darkVoidId = DimensionManager.getNextFreeDimId();
        darkVoidType = DimensionType.register("Dark Void", "_darkVoid", darkVoidId, WorldProviderDarkVoid.class, false);
        DimensionManager.registerDimension(darkVoidId, darkVoidType);

        lightVoidId = DimensionManager.getNextFreeDimId();
        lightVoidType = DimensionType.register("Light Void", "_lightVoid", lightVoidId, WorldProviderLightVoid.class, false);
        DimensionManager.registerDimension(lightVoidId, lightVoidType);
    }

    public static TeleporterRTK getTeleporterDark(World world) {
        if (teleporterDark != null) {
            return teleporterDark;
        } else {
            teleporterDark = new TeleporterRTK(world.getMinecraftServer().getWorld(darkVoidId));
            return teleporterDark;
        }
    }

    public static TeleporterRTK getTeleporterLight(World world) {
        if (teleporterLight != null) {
            return teleporterLight;
        } else {
            teleporterLight = new TeleporterRTK(world.getMinecraftServer().getWorld(lightVoidId));
            return teleporterLight;
        }
    }

}
