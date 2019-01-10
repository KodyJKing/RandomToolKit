package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.item.ModItems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ModBlocks {
    private static List<Block> toRegister = new ArrayList<>();
    public static BlockBase levitator, hole, fourierTransformer, tent, emergencyTent, diversTent, enderTent, diversEnderTent;
    public static BlockTentWall tentWall;

    public void init() {
        levitator = add(new BlockLevitator("levitator"));
        hole = add(new BlockHole("hole"));
        fourierTransformer = add(new BlockFourierTransformer("fouriertransformer"));
        tent = add(new BlockTentBreakable("tent"));
        emergencyTent = add(new BlockEmergencyTent("emergencytent"));
        diversTent = add(new BlockDiversTent("diverstent"));
        enderTent = add(new BlockEnderTent("endertent"));
        diversEnderTent = add(new BlockDiversEnderTent("diversendertent"));
        tentWall = add(new BlockTentWall("tentwall"));
    }

    @SubscribeEvent
    public void onItemRegistry(RegistryEvent.Register<Block> event) {
        init();
//        System.out.println("RTK is registering blocks...");
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Block block: toRegister) registry.register(block);
        toRegister.clear();
    }

    static HashSet<Class<? extends TileEntity>> registeredTEs = new HashSet<>();
    public static <T extends Block> T add(T block) {
        toRegister.add(block);

        ItemBlock itemBlock;
        if (block instanceof BlockBase)
            itemBlock = ((BlockBase)block).createItemBlock(block);
        else
            itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        ModItems.add(itemBlock);

        if (block instanceof BlockBase) {
            BlockBase b = (BlockBase)block;

//            b.init(itemBlock);

            if (b.hasTileEntity() && !registeredTEs.contains(b.getTileEntityClass())) {
                registeredTEs.add(b.getTileEntityClass());
                GameRegistry.registerTileEntity(b.getTileEntityClass(), b.getUnlocalizedName());
//                System.out.println("Registered tile entity: " + b.getUnlocalizedName());
            }
        }

        return block;
    }

}
