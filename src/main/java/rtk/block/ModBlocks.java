package rtk.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    private static List<Block> toRegister = new ArrayList<>();

    public static Block tent,
            tentWall, emergencyTentWall, emergencyTentLight, enderTentWall, diversTentWall, diversEnderTentWall;

    public static void init() {
        tentWall = addTentWall("tentwall");
        emergencyTentWall = addTentWall("emergencytentwall");
        emergencyTentLight = addTentWall("emergencytentlight");
        enderTentWall = addTentWall("endertentwall");
        diversTentWall = addTentWall("diverstentwall");
        diversEnderTentWall = addTentWall("diversendertentwall");
        tent = add("tent", new BlockTent(tentWall.getDefaultState(), tentWall.getDefaultState()), new Item.Properties().group(ItemGroup.TRANSPORTATION));
    }

    public static <T extends Block> T add(String registryName, T block, Item.Properties itemProperties) {
        block.setRegistryName(registryName);
        toRegister.add(block);
        if (itemProperties != null)
            ModItems.add(registryName, new BlockItem(block, itemProperties));
        return block;
    }

    public static <T extends Block> T add(String registryName, T block) {
        return add(registryName, block, null);
    }

    private static Block addTentWall(String registryName) {
        Block block = new Block(
                AbstractBlock.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(.5f, .5f));
        return add(registryName, block, new Item.Properties().group(ItemGroup.TRANSPORTATION));
    }

    public static void register(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Block block : toRegister)
            registry.register(block);
        toRegister = null;
    }

}
