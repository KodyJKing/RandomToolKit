package rtk.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.RTK;
import rtk.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    private static List<Block> toRegister = new ArrayList<>();

    public static Block tent, emergencyTent, diversTent, enderTent, diversEnderTent,
            tentWall, emergencyTentWall, emergencyTentLight, enderTentWall, diversTentWall, diversEnderTentWall;

    public static void init() {
        tentWall = addTentWall("tentwall", false);
        emergencyTentWall = addTentWall("emergencytentwall", false);
        emergencyTentLight = addTentWall("emergencytentlight", true);
        enderTentWall = addTentWall("endertentwall", false);
        diversTentWall = addTentWall("diverstentwall", false);
        diversEnderTentWall = addTentWall("diversendertentwall", false);

        tent = add(
                "tent",
                new BlockTent( AbstractBlock.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(.5f)),
                new Item.Properties().group(ItemGroup.TRANSPORTATION)
        );
        emergencyTent = add(
                "emergencytent",
                new BlockTentBreakable(
                        AbstractBlock.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(.5f).noDrops()
                ) { {
                    wall = emergencyTentWall;
                    light = emergencyTentLight;
                    fuelCost = 0;
                } },
                new Item.Properties().group(ItemGroup.TRANSPORTATION)
        );
        diversTent = add(
                "diverstent",
                new BlockTentBreakable(
                        AbstractBlock.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(.5f)
                ) {
                    {
                        wall = diversTentWall;
                        light = diversTentWall;
                        fuelCost = 32;
                    }
                },
                new Item.Properties().group(ItemGroup.TRANSPORTATION)
        );
        enderTent = add(
                "endertent",
                new BlockEnderTent(
                        AbstractBlock.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(.5f).noDrops()
                ) {
                    {
                        wall = enderTentWall;
                        light = enderTentWall;
                    }
                },
                new Item.Properties().group(ItemGroup.TRANSPORTATION)
        );
        diversEnderTent = add(
                "diversendertent",
                new BlockEnderTent(
                        AbstractBlock.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(.5f).noDrops()
                ) {
                    {
                        wall = diversEnderTentWall;
                        light = diversEnderTentWall;
                        fuelCost = 16;
                    }
                },
                new Item.Properties().group(ItemGroup.TRANSPORTATION)
        );
    }

    public static <T extends Block> T add(String registryName, T block, Item.Properties itemProperties) {
        block.setRegistryName(registryName);
        toRegister.add(block);
        if (itemProperties != null) {
//            RTK.LOGGER.info("Queued registrations for block item: " + registryName);
            ModItems.add(registryName, new BlockItem(block, itemProperties));
        }
        return block;
    }

    public static <T extends Block> T add(String registryName, T block) {
        return add(registryName, block, null);
    }

    private static Block addTentWall(String registryName, boolean isLight) {
        AbstractBlock.Properties properties =
                AbstractBlock.Properties.create(Material.WOOL)
                .sound(SoundType.CLOTH)
                .hardnessAndResistance(.5f, .5f)
                .noDrops();
        if (isLight)
            properties.setLightLevel((bs) -> 15);
        return add(registryName, new Block(properties), new Item.Properties());
    }

    public static void register(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Block block : toRegister)
            registry.register(block);
        toRegister = null;
    }

}
