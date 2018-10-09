package rtk.dimension;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;
import rtk.ModConfig;

public class ModDimensions {
    public static int darkVoidId, lightVoidId;
    public static DimensionType darkVoidType, lightVoidType;

    public static void init() {
        if (ModConfig.dimension.darkvoidid == 0)
            darkVoidId = DimensionManager.getNextFreeDimId();
        else
            darkVoidId = ModConfig.dimension.darkvoidid;
        darkVoidType = DimensionType.register("Dark Void", "_darkVoid", darkVoidId, WorldProviderDarkVoid.class, false);
        DimensionManager.registerDimension(darkVoidId, darkVoidType);

        if (ModConfig.dimension.lightvoidid == 0)
            lightVoidId = DimensionManager.getNextFreeDimId();
        else
            lightVoidId = ModConfig.dimension.lightvoidid;
        lightVoidType = DimensionType.register("Light Void", "_lightVoid", lightVoidId, WorldProviderLightVoid.class, false);
        DimensionManager.registerDimension(lightVoidId, lightVoidType);
    }

    public static void teleportPlayer(EntityPlayer player, int dimensionID, ITeleporter teleporter) {
        player.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP)player, dimensionID, teleporter);
    }
}
