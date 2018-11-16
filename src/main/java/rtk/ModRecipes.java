package rtk;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import rtk.block.ModBlocks;
import rtk.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class ModRecipes {
    private static List<IRecipe> toRegister = new ArrayList<>();

    public void init() {

        add(shapedRecipe(new ItemStack(ModBlocks.emergencyTent),
                "OLO",
                "LCL",
                "OLO", 'O', new ItemStack(Items.DYE, 1, 14), 'L', Items.LEATHER, 'C', new ItemStack(Items.COAL, 1, 1)));

        add(shapedRecipe(new ItemStack(ModBlocks.tent),
                "ILI",
                "LEL",
                "IFI", 'L', Items.LEATHER, 'E', ModBlocks.emergencyTent, 'I', Items.IRON_INGOT, 'F', Blocks.FURNACE));

        add(shapedRecipe(new ItemStack(ModBlocks.diversTent),
                "ISI",
                "SES",
                "IFI", 'S', Items.SLIME_BALL, 'E', ModBlocks.tent, 'I', Blocks.IRON_BLOCK, 'F', Blocks.FURNACE));


        add(shapedRecipe(new ItemStack(ModBlocks.enderTent),
                "BEB",
                "ETE",
                "BCB", 'E', Blocks.END_STONE, 'B', Items.BLAZE_ROD, 'T', ModBlocks.tent, 'C', Blocks.ENDER_CHEST));

        add(shapedRecipe(new ItemStack(ModBlocks.diversEnderTent),
                "BEB",
                "ETE",
                "BCB", 'E', Blocks.END_STONE, 'B', Items.BLAZE_ROD, 'T', ModBlocks.diversTent, 'C', Blocks.ENDER_CHEST));

        add(shapedRecipe(new ItemStack(ModBlocks.diversEnderTent),
                "ISI",
                "SES",
                "IFI", 'S', Items.SLIME_BALL, 'E', ModBlocks.enderTent, 'I', Blocks.IRON_BLOCK, 'F', Blocks.FURNACE));

        add(shapedRecipe(new ItemStack(ModItems.trowel),
                "II ",
                "IS ",
                "  S", 'I', Items.IRON_INGOT, 'S', Items.STICK));

        add(shapedRecipe(new ItemStack(ModItems.hotplate),
                "III",
                "III",
                " S ", 'I', Items.IRON_INGOT, 'S', Items.STICK));

        add(shapedRecipe(new ItemStack(ModItems.toolbelt),
                "LLL",
                "L L",
                "LIL", 'L', Items.LEATHER, 'I', Items.IRON_INGOT));

        add(shapedRecipe(new ItemStack(ModItems.toolbox),
                "ILI",
                "ICC",
                "III", 'L', Items.LEATHER, 'I', Items.IRON_INGOT, 'C', Blocks.CHEST));

        add(shapedRecipe(new ItemStack(ModBlocks.levitator),
                " F ",
                "QQQ",
                "QRQ", 'F', Items.FEATHER, 'Q', Items.QUARTZ, 'R', Items.REDSTONE));

        add(shapedRecipe(new ItemStack(ModItems.dolly),
                "I  ",
                "IC ",
                "II ", 'I', Items.IRON_INGOT, 'C', Blocks.CHEST));

        add(shapedRecipe(new ItemStack(ModItems.earthStrider),
                "SSE",
                "SED",
                "EDN", 'S', Items.STRING, 'E', Items.ENDER_EYE, 'D', Blocks.DIAMOND_BLOCK, 'N', Items.NETHER_STAR));

        add(shapedRecipe(new ItemStack(ModItems.barometer),
                "GGG",
                "G G",
                "GWG", 'G', Blocks.GLASS, 'W', Items.WATER_BUCKET));

        add(shapedRecipe(new ItemStack(ModBlocks.fourierTransformer),
                "RRR",
                "PCE",
                "RRR", 'R', Items.REPEATER, 'P', Items.ENDER_PEARL, 'C', Items.COMPARATOR, 'E', Items.ENDER_EYE));

        add(shapedRecipe(new ItemStack(ModItems.netherPearl),
                "OOO",
                "OBO",
                "OOO", 'O', Blocks.OBSIDIAN, 'B', Items.BLAZE_POWDER));

        add(shapedRecipe(new ItemStack(ModItems.stopwatch),
                " B ",
                " CB",
                "   ", 'B', Blocks.STONE_BUTTON, 'C', Items.CLOCK));

        add(shapelessRecipe(new ItemStack(ModItems.hotplateEtched), ModItems.hotplate, Blocks.STONEBRICK, Items.DIAMOND, Items.DIAMOND));
        add(shapelessRecipe(new ItemStack(ModItems.needle), Items.IRON_INGOT, Items.STRING));
        add(shapelessRecipe(new ItemStack(ModItems.eyeOfNether), ModItems.netherPearl, Items.BLAZE_ROD));
        add(shapelessRecipe(new ItemStack(ModItems.hotSword), Items.IRON_SWORD, Items.LAVA_BUCKET));
        if (ModConfig.earthStriderRepairable)
            add(shapelessRecipe(new ItemStack(ModItems.earthStrider), ModItems.earthStriderDrained, Items.ENDER_EYE, Items.DIAMOND, Items.DIAMOND, Items.DIAMOND, Items.DIAMOND, Items.DIAMOND, Items.DIAMOND));

//        GameRegistry.addSmelting(new ItemStack(Items.IRON_SWORD), new ItemStack(ModItems.hotSword, 1, 3), 0);
//        for (int i = 1; i <= 3; i++)
//            GameRegistry.addSmelting(new ItemStack(ModItems.hotSword, 1, i), new ItemStack(ModItems.hotSword, 1, i - 1), 0);
    }

    @SubscribeEvent
    public void onCraftingRegistry(Register<IRecipe> event) {
        init();
//        System.out.println("RTK is registering recipes...");
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        for (IRecipe recipe: toRegister) registry.register(recipe);
        toRegister.clear();
    }

    public static <T extends IRecipe> T add(T recipe) {
        recipe.setRegistryName(new ResourceLocation(recipe.getGroup()));
        toRegister.add(recipe);
        return recipe;
    }

    int i = 0;
    private String nextGroupName() {
        return new ResourceLocation(RTK.modId, "recipes" + (i++)).toString();
    }

    public IRecipe shapedRecipe(ItemStack output, Object... input) {
        ShapedPrimer primer = CraftingHelper.parseShaped(input);
        return new ShapedRecipes(nextGroupName(), primer.width, primer.height, primer.input, output);
    }

    public IRecipe shapelessRecipe(ItemStack output, Object... input) {
        NonNullList<Ingredient> ingredients = toIngredients(input);
        return new ShapelessRecipes(nextGroupName(), output, ingredients);
    }

    // Based on code by Ellpeck for Actually Additions
    // https://github.com/Ellpeck/ActuallyAdditions/blob/master/src/main/java/de/ellpeck/actuallyadditions/mod/util/crafting/RecipeHelper.java
    public NonNullList<Ingredient> toIngredients(Object[] input) {
        NonNullList<Ingredient> result = NonNullList.create();
        for (Object v: input) {
            if (v instanceof String) {
                result.add(new OreIngredient((String)v));
            } else {
                ItemStack stack;
                if (v instanceof ItemStack) stack = (ItemStack) v;
                else if (v instanceof Item) stack = new ItemStack((Item)v);
                else if (v instanceof Block) stack = new ItemStack((Block)v);
                else throw new UnsupportedOperationException("Invalid shapeless recipe!");
                result.add(Ingredient.fromStacks(stack));
            }
        }
        return result;
    }

}
