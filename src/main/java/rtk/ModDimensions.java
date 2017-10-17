package rtk;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import rtk.dimension.TeleporterRTK;
import rtk.dimension.WorldProviderDarkVoid;

public class ModDimensions {

    public static int darkVoidId;
    public static DimensionType darkVoidType;

    public static TeleporterRTK teleporter;

    public static void init() {
        darkVoidId = DimensionManager.getNextFreeDimId();
        darkVoidType = DimensionType.register("Dark Void", "_darkVoid", darkVoidId, WorldProviderDarkVoid.class, false);
        DimensionManager.registerDimension(darkVoidId, darkVoidType);
    }

    public static TeleporterRTK getTeleporter(World world) {
        if (teleporter != null) {
            return teleporter;
        } else {
            teleporter = new TeleporterRTK(world.getMinecraftServer().worldServerForDimension(darkVoidId));
            return teleporter;
        }
    }

}
