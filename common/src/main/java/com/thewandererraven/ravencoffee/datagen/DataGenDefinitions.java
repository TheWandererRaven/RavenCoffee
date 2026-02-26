package com.thewandererraven.ravencoffee.datagen;

import com.thewandererraven.ravencoffee.block.BlocksRegistry;
import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import com.thewandererraven.ravencoffee.util.RavenCoffeeTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataGenDefinitions {
    public static List<DataGenItem> ITEMS = new ArrayList<>();
    public static List<DataGenBlock> BLOCKS = new ArrayList<>();

    public static void buildRecipes(HolderGetter<Item> items, RecipeOutput output) {
        // #################################### GENERAL ITEMS
        // ################ COFFEE BEANS
        SimpleCookingRecipeBuilder.generic(
                Ingredient.of(GeneralItemsRegistry.COFFEE_CHERRIES.get()),
                RecipeCategory.FOOD,
                GeneralItemsRegistry.ROASTED_COFFEE_BEANS.get(),
                0.05f,
                100,
                RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new
                )
                .unlockedBy("has_coffee_cherries",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, GeneralItemsRegistry.COFFEE_CHERRIES.get()).build()
                        ))
                .save(output);
        // ###### COFFEE GRINDER
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD,
                        GeneralItemsRegistry.COFFEE_GRINDER_ITEM.get()
                )
                .pattern("pip")
                .pattern("fff")
                .pattern("ppp")
                .define('i', Items.IRON_INGOT)
                .define('f', Items.FLINT)
                .define('p', ItemTags.PLANKS)
                .unlockedBy("has_iron_and_flint",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                        .of(items, Items.IRON_INGOT, Items.FLINT)
                                        .build(),
                                ItemPredicate.Builder.item()
                                        .of(items, GeneralItemsRegistry.GROUND_COFFEE.get())
                                        .build()
                        ))
                .save(output);
        // ###### COFFEE MACHINE
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD,
                        GeneralItemsRegistry.COFFEE_MACHINE_ITEM.get()
                )
                .pattern("igi")
                .pattern("gwg")
                .pattern("iri")
                .define('i', Items.IRON_INGOT)
                .define('g', Items.GLASS_PANE)
                .define('w', Items.WATER_BUCKET)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_redstone_and_coffee",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                        .of(items, Items.REDSTONE, Items.IRON_INGOT)
                                        .build(),
                                ItemPredicate.Builder.item()
                                        .of(items, GeneralItemsRegistry.GROUND_COFFEE.get())
                                        .build()
                        ))
                .save(output);
        // ################ FOOD
        // ###### BROWNIE
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.BROWNIE.get(), 1)
                .requires(Items.WHEAT, 2)
                .requires(Items.COCOA_BEANS, 2)
                .unlockedBy("has_cocoa_beans",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COCOA_BEANS).build()
                        ))
                .save(output);
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD,
                        GeneralItemsRegistry.BROWNIE_BLOCK_ITEM.get()
                )
                .pattern("bbb")
                .pattern("bbb")
                .pattern("bbb")
                .define('b', GeneralItemsRegistry.BROWNIE.get())
                .unlockedBy("has_cocoa_beans",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COCOA_BEANS).build()
                        ))
                .save(output);
        // ###### SANDWICH
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.CHICKEN_SANDWICH.get(), 1)
                .requires(Items.BREAD, 1)
                .requires(Items.COOKED_CHICKEN, 1)
                .unlockedBy("has_bread_and_chicken",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COOKED_CHICKEN, Items.BREAD)
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.HAM_SANDWICH.get(), 1)
                .requires(Items.BREAD, 1)
                .requires(Items.PORKCHOP, 1)
                .unlockedBy("has_bread_and_porkchop",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.PORKCHOP, Items.BREAD)
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.BEEF_SANDWICH.get(), 1)
                .requires(Items.BREAD, 1)
                .requires(Items.COOKED_BEEF, 1)
                .unlockedBy("has_bread_and_beef",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COOKED_BEEF, Items.BREAD)
                                .build()
                        ))
                .save(output);
        // ###### CROISSANT
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD,
                        GeneralItemsRegistry.CROISSANT.get()
                )
                .pattern(" w ")
                .pattern("w w")
                .define('w', Items.WHEAT)
                .unlockedBy("has_wheat",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.WHEAT)
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.CHICKEN_CROISSANT.get(), 1)
                .requires(GeneralItemsRegistry.CROISSANT.get(), 1)
                .requires(Items.COOKED_CHICKEN, 1)
                .unlockedBy("has_croissant_and_chicken",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COOKED_CHICKEN, GeneralItemsRegistry.CROISSANT.get())
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.HAM_CROISSANT.get(), 1)
                .requires(GeneralItemsRegistry.CROISSANT.get(), 1)
                .requires(Items.PORKCHOP, 1)
                .unlockedBy("has_croissant_and_porkchop",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.PORKCHOP, GeneralItemsRegistry.CROISSANT.get())
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.BEEF_CROISSANT.get(), 1)
                .requires(GeneralItemsRegistry.CROISSANT.get(), 1)
                .requires(Items.COOKED_BEEF, 1)
                .unlockedBy("has_croissant_and_beef",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COOKED_BEEF, GeneralItemsRegistry.CROISSANT.get())
                                .build()
                        ))
                .save(output);
        // ###### BAGEL
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD,
                        GeneralItemsRegistry.BAGEL.get()
                )
                .pattern(" w ")
                .pattern("w w")
                .pattern(" w ")
                .define('w', Items.WHEAT)
                .unlockedBy("has_wheat",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.WHEAT)
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.CHICKEN_BAGEL.get(), 1)
                .requires(GeneralItemsRegistry.BAGEL.get(), 1)
                .requires(Items.COOKED_CHICKEN, 1)
                .unlockedBy("has_bagel_and_chicken",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COOKED_CHICKEN, GeneralItemsRegistry.BAGEL.get())
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.HAM_BAGEL.get(), 1)
                .requires(GeneralItemsRegistry.BAGEL.get(), 1)
                .requires(Items.PORKCHOP, 1)
                .unlockedBy("has_bagel_and_porkchop",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.PORKCHOP, GeneralItemsRegistry.BAGEL.get())
                                .build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.BEEF_BAGEL.get(), 1)
                .requires(GeneralItemsRegistry.BAGEL.get(), 1)
                .requires(Items.COOKED_BEEF, 1)
                .unlockedBy("has_bagel_and_beef",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COOKED_BEEF, GeneralItemsRegistry.BAGEL.get())
                                .build()
                        ))
                .save(output);
        // ###### ECLAIR
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD,
                        GeneralItemsRegistry.COFFEE_ECLAIR.get()
                )
                .pattern("ccc")
                .pattern("rrr")
                .pattern("www")
                .define('c', Items.COCOA_BEANS)
                .define('r', GeneralItemsRegistry.GROUND_COFFEE.get())
                .define('w', Items.WHEAT)
                .unlockedBy("has_wheat",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.COCOA_BEANS, GeneralItemsRegistry.GROUND_COFFEE.get())
                                .build()
                        ))
                .save(output);
        // ###### POPCHORUS
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.POPCHORUS.get(), 1)
                .requires(Items.BOWL, 1)
                .requires(Items.POPPED_CHORUS_FRUIT, 3)
                .unlockedBy("has_chorus_fruit",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.CHORUS_FRUIT)
                                .build()
                        ))
                .save(output);
        // ###### MELON PAN
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD,
                        GeneralItemsRegistry.MELON_PAN.get()
                )
                .pattern(" w ")
                .pattern("wmw")
                .pattern(" w ")
                .define('w', Items.WHEAT)
                .define('m', Items.MELON_SLICE)
                .unlockedBy("has_melon",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.MELON)
                                .build()
                        ))
                .save(output);
        // ###### MUFFIN
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, GeneralItemsRegistry.MUFFIN.get(), 1)
                .requires(Items.WHEAT, 1)
                .requires(Items.PAPER, 1)
                .unlockedBy("has_wheat",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, Items.WHEAT)
                                .build()
                        ))
                .save(output);
        // ################ MISC
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, Items.BONE_MEAL, 1)
                .requires(GeneralItemsRegistry.COFFEE_CHERRIES.get(), 9)
                .unlockedBy("has_coffee_cherries",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, GeneralItemsRegistry.COFFEE_CHERRIES.get()).build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, Items.RED_DYE, 1)
                .requires(GeneralItemsRegistry.COFFEE_CHERRIES.get(), 1)
                .unlockedBy("has_coffee_cherries",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, GeneralItemsRegistry.COFFEE_CHERRIES.get()).build()
                        ))
                .save(output);
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, Items.BROWN_DYE, 1)
                .requires(GeneralItemsRegistry.ROASTED_COFFEE_BEANS.get(), 1)
                .unlockedBy("has_coffee_cherries",
                        inventoryTrigger(ItemPredicate.Builder.item()
                                .of(items, GeneralItemsRegistry.COFFEE_CHERRIES.get()).build()
                        ))
                .save(output);
    }

    private static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... predicates) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates)));
    }

    static {
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.DEBUG_MUG.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.COFFEE_CHERRIES.get())
                .setCompostableValue(0.65f)
        );
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.COFFEE_BEANS.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.ROASTED_COFFEE_BEANS.get()).withTag(RavenCoffeeTags.Items.ROASTED_COMMON_BEANS));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.MAGMA_COFFEE_BEANS.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.GROUND_COFFEE.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.GROUND_MAGMA_COFFEE.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.POPCHORUS.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.MUFFIN.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.MELON_PAN.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.COFFEE_ECLAIR.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.BROWNIE.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.TIRAMISU_SLICE.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.HAM_SANDWICH.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.BEEF_SANDWICH.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.CHICKEN_SANDWICH.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.CROISSANT.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.HAM_CROISSANT.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.BEEF_CROISSANT.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.CHICKEN_CROISSANT.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.BAGEL.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.HAM_BAGEL.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.BEEF_BAGEL.get()));
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.CHICKEN_BAGEL.get()));

        ITEMS.add(new DataGenItem(GeneralItemsRegistry.COFFEE_MUG.get()).setAlternateTextureResourceLocation("item/coffee_mug/empty"));
        //TODO: Add data gen for the Coffee Brew and its select model

        BLOCKS.add(new DataGenBlock(BlocksRegistry.BROWNIE_BLOCK.get())
                        .setOnlyDropsSelf()
//                        .withLootTable(
//                                LootTable.lootTable().withPool(
//                                        LootPool.lootPool()
////                                        .when(
////                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(BlocksRegistry.BROWNIE_BLOCK.get())
////                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty())
//                                                .add(LootItem.lootTableItem(GeneralItemsRegistry.ROASTED_COFFEE_BEANS.get()))
//                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 3.0f)))
//                                        )
//                                        .withPool(
//                                                LootPool.lootPool()
//                                                        .add(LootItem.lootTableItem(GeneralItemsRegistry.GROUND_COFFEE.get()))
//                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(10.0f, 30.0f)))
//                                )
//                        )
        );
        BLOCKS.add(new DataGenBlock(BlocksRegistry.COFFEE_GRINDER.get())
                .setOnlyDropsSelf()
                .setBlockModelGenerationType(DataGenBlock.BlockModelGenTypes.NO_TEMPLATE_HORIZONTALLY_ROTATION)
        );
        BLOCKS.add(new DataGenBlock(BlocksRegistry.COFFEE_MACHINE.get())
                .setOnlyDropsSelf()
                .setBlockModelGenerationType(DataGenBlock.BlockModelGenTypes.IGNORE)
        );
        ITEMS.add(new DataGenItem(GeneralItemsRegistry.COFFEE_MACHINE_ITEM.get())
                .setItemModelGenerationType(DataGenItem.ItemModelGenTypes.BLOCK)
                .setBlockItemPlainModelFromBlock("_inactive_nocups_nooutput_input")
        );
    }
}
