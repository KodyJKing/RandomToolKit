package rtk;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rtk.block.*;

public class ModBlocks {
    public static BlockBase
            emergencyTent, emergencyTentWall, emergencyTentLight,
            tent, tentWall,
            diversTent, diversTentWall,
            enderTent, enderTentWall,
            transientBlock;

    public static void init() {

        emergencyTent = register(new BlockEmergencyTent("emergencyTent"));
        emergencyTentWall = register(new BlockTentWall("emergencyTentWall"));
        emergencyTentLight = (BlockBase)register(new BlockTentWall("emergencyTentLight")).setLightLevel(1);

        tent = register(new BlockTent("tent"));
        tentWall = register(new BlockTentWall("tentWall"));

        diversTent = register(new BlockDiversTent("diversTent"));
        diversTentWall = register(new BlockTentWall("diversTentWall"));

        enderTent = register(new BlockEnderTent("enderTent"));
        enderTentWall = register(new BlockTentWall("enderTentWall"));

        //transientBlock = register(new BlockTransient("transientBLock"));
    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof BlockBase) {
            BlockBase b = (BlockBase)block;
            b.registerItemModel(itemBlock);
            if(b.hasTileEntity()){
                GameRegistry.registerTileEntity(b.getTileEntityClass(), b.getUnlocalizedName());
            }
        }

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock;
        if(block instanceof  BlockBase)
            itemBlock = ((BlockBase)block).createItemBlock(block);
        else
            itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }

}
