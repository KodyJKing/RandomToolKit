package rtk.dimension;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;

public class ModDimensions {
    public static int darkVoidId, lightVoidId;
    public static DimensionType darkVoidType, lightVoidType;

    public static void init() {
        darkVoidId = DimensionManager.getNextFreeDimId();
        darkVoidType = DimensionType.register("Dark Void", "_darkVoid", darkVoidId, WorldProviderDarkVoid.class, false);
        DimensionManager.registerDimension(darkVoidId, darkVoidType);

        lightVoidId = DimensionManager.getNextFreeDimId();
        lightVoidType = DimensionType.register("Light Void", "_lightVoid", lightVoidId, WorldProviderLightVoid.class, false);
        DimensionManager.registerDimension(lightVoidId, lightVoidType);
    }

    public static void teleportPlayer(EntityPlayer player, int dimensionID, ITeleporter teleporter) {
        player.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP)player, dimensionID, teleporter);
    }
}
