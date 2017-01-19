package rtk;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
    public static void buildRecipes(){
        System.out.println("Building recipes for " + RTK.name + ".");

        GameRegistry.addRecipe(new ItemStack(ModBlocks.emergencyTent),
                "OLO",
                "LCL",
                "OLO", 'O', new ItemStack(Items.DYE, 1, 14), 'L', Items.LEATHER, 'C', new ItemStack(Items.COAL, 1, 1));

        GameRegistry.addRecipe(new ItemStack(ModBlocks.tent),
                "ILI",
                "LEL",
                "IFI", 'L', Items.LEATHER, 'E', ModBlocks.emergencyTent, 'I', Items.IRON_INGOT, 'F', Blocks.FURNACE);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.diversTent),
                "ISI",
                "SES",
                "IFI", 'S', Items.SLIME_BALL, 'E', ModBlocks.tent, 'I', Blocks.IRON_BLOCK, 'F', Blocks.FURNACE);


        GameRegistry.addRecipe(new ItemStack(ModBlocks.enderTent, 1),
                "BEB",
                "ETE",
                "BCB", 'E', Blocks.END_STONE, 'B', Items.BLAZE_ROD, 'T', ModBlocks.tent, 'C', Blocks.ENDER_CHEST);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.diversEnderTent, 1),
                "BEB",
                "ETE",
                "BCB", 'E', Blocks.END_STONE, 'B', Items.BLAZE_ROD, 'T', ModBlocks.diversTent, 'C', Blocks.ENDER_CHEST);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.diversEnderTent),
                "ISI",
                "SES",
                "IFI", 'S', Items.SLIME_BALL, 'E', ModBlocks.enderTent, 'I', Blocks.IRON_BLOCK, 'F', Blocks.FURNACE);

        GameRegistry.addRecipe(new ItemStack(ModItems.trowel, 1),
                "II#",
                "IS#",
                "##S", 'I', Items.IRON_INGOT, 'S', Items.STICK);

        GameRegistry.addRecipe(new ItemStack(ModItems.hotplate, 1),
                "III",
                "III",
                "#S#", 'I', Items.IRON_INGOT, 'S', Items.STICK);

        GameRegistry.addRecipe(new ItemStack(ModItems.toolbelt, 1),
                "LLL",
                "L#L",
                "LIL", 'L', Items.LEATHER, 'I', Items.IRON_INGOT);

        GameRegistry.addRecipe(new ItemStack(ModItems.toolbox, 1),
                "ILI",
                "ICC",
                "III", 'L', Items.LEATHER, 'I', Items.IRON_INGOT, 'C', Blocks.CHEST);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.levitator, 1),
                "#F#",
                "QQQ",
                "QRQ", 'F', Items.FEATHER, 'Q', Items.QUARTZ, 'R', Items.REDSTONE);

        GameRegistry.addRecipe(new ItemStack(ModItems.dolly, 1),
                "I##",
                "IC#",
                "II#", 'I', Items.IRON_INGOT, 'C', Blocks.CHEST);

        GameRegistry.addRecipe(new ItemStack(ModItems.earthStrider, 1),
                "EDE",
                "ENE",
                "EEE", 'E', Items.ENDER_EYE, 'D', Items.DIAMOND_PICKAXE, 'N', Items.NETHER_STAR);

        GameRegistry.addRecipe(new ItemStack(ModItems.barometer, 1),
                "GGG",
                "G#G",
                "GWG", 'G', Blocks.GLASS, 'W', Items.WATER_BUCKET);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.fourierTransformer, 1),
                "RRR",
                "PCE",
                "RRR", 'R', Items.REPEATER, 'P', Items.ENDER_PEARL, 'C', Items.COMPARATOR, 'E', Items.ENDER_EYE);

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.hotplateEtched), new Object[] {ModItems.hotplate, Blocks.STONEBRICK, Items.DIAMOND, Items.DIAMOND});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.needle), new Object[] {Items.IRON_INGOT, Items.STRING});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.earthStrider), new Object[] {ModItems.earthStriderDrained, Items.ENDER_EYE, Items.DIAMOND});

        GameRegistry.addSmelting(new ItemStack(Items.IRON_SWORD), new ItemStack(ModItems.hotSword, 1, 3), 0);
        for(int i = 1; i <= 3; i++)
            GameRegistry.addSmelting(new ItemStack(ModItems.hotSword, 1, i), new ItemStack(ModItems.hotSword, 1, i - 1), 0);

    }
}
