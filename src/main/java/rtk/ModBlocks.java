package rtk;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.block.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ModBlocks {
    private static List<Block> toRegister = new ArrayList<>();
    public static BlockBase levitator, hole, fourierTransformer;

    public ModBlocks() {
        levitator = add(new BlockLevitator("levitator"));
        hole = add(new BlockHole("hole"));
        fourierTransformer = add(new BlockFourierTransformer("fouriertransformer"));
    }

    @SubscribeEvent
    public void onItemRegistry(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Block block: toRegister) registry.register(block);
    }

    static HashSet<Class<? extends TileEntity>> registeredTEs = new HashSet<>();
    public static <T extends Block> T add(T block) {
        toRegister.add(block);

        ItemBlock itemBlock;
        if(block instanceof  BlockBase)
            itemBlock = ((BlockBase)block).createItemBlock(block);
        else
            itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        ModItems.add(itemBlock);

        if (block instanceof BlockBase) {
            BlockBase b = (BlockBase)block;

//            b.init(itemBlock);

            if(b.hasTileEntity() && !registeredTEs.contains(b.getTileEntityClass())){
                registeredTEs.add(b.getTileEntityClass());
                GameRegistry.registerTileEntity(b.getTileEntityClass(), b.getUnlocalizedName());
//                System.out.println("Registered tile entity: " + b.getUnlocalizedName());
            }
        }

        return block;
    }

}
