package rtk.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import rtk.block.ModBlocks;

public class ModTileTypes {

    public static TileEntityType<?> enderTent = TileEntityType.Builder.create( TileEnderTent::new, ModBlocks.enderTent, ModBlocks.diversEnderTent).build(null).setRegistryName("endertent");

    public static void register(final RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(enderTent);
    }

}
