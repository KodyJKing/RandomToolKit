package rtk;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rtk.block.*;

public class ModBlocks {
    public static BlockBase
            emergencyTent,
            tent,
            diversTent,
            enderTent;

    public static BlockTentWall tentWall;

    public static void init() {

        emergencyTent = register(new BlockEmergencyTent("emergencyTent"));

        tent = register(new BlockTentBreakable("tent"));

        diversTent = register(new BlockDiversTent("diversTent"));

        enderTent = register(new BlockEnderTent("enderTent"));

        tentWall = register(new BlockTentWall("tentWall"));
    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof BlockBase) {
            BlockBase b = (BlockBase)block;
            b.init(itemBlock);
            if(b.hasTileEntity())
                GameRegistry.registerTileEntity(b.getTileEntityClass(), b.getUnlocalizedName());
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
