package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.block.BlocksRegistry;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GeneralItemsRegistry {
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.get(Registries.ITEM, Constants.MOD_ID);

    // ############################################### BASIC COFFEE STUFF ##############################################
    public static final String _coffee_cherries_id = "coffee_cherries";
    //TODO: MAKE SEEDS
    public static final RegistryObject<Item> COFFEE_CHERRIES =
            ITEMS.register(_coffee_cherries_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_cherries_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(1)
                            .saturationModifier(0.05f)
                            .build()
                    )

            ));

    public static final String _coffee_beans_id = "coffee_beans";
    public static final RegistryObject<Item> COFFEE_BEANS =
            ITEMS.register(_coffee_beans_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_beans_id)
                    ))
            ));

    public static final String _roasted_coffee_beans_id = "roasted_coffee_beans";
    public static final RegistryObject<Item> ROASTED_COFFEE_BEANS =
            ITEMS.register(_roasted_coffee_beans_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _roasted_coffee_beans_id)
                    ))
            ));

    public static final String _magma_coffee_beans_id = "magma_coffee_beans";
    public static final RegistryObject<Item> MAGMA_COFFEE_BEANS =
            ITEMS.register(_magma_coffee_beans_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _magma_coffee_beans_id)
                    ))
            ));

    // ############################################### COFFEE INGREDIENTS ##############################################

    public static final String _ground_coffee_id = "ground_coffee";
    public static final RegistryObject<Item> GROUND_COFFEE =
            ITEMS.register(_ground_coffee_id, () -> new BoneMealItem(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _ground_coffee_id)
                    ))
            ));

    public static final String _ground_magma_coffee_id = "ground_magma_coffee";
    public static final RegistryObject<Item> GROUND_MAGMA_COFFEE =
            ITEMS.register(_ground_magma_coffee_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _ground_magma_coffee_id)
                    ))
            ));

    // ############################################### FOODSTUFF ##############################################
    public static final String _popchorus_coffee_id = "popchorus";
    // TODO: Create popchorus food item
    public static final RegistryObject<Item> POPCHORUS =
            ITEMS.register(_popchorus_coffee_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _popchorus_coffee_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(12)
                            .saturationModifier(0.15f)
                            .build()
                    )
            ));
    public static final String _muffin_id = "muffin";
    // TODO: Create muffin food item
    public static final RegistryObject<Item> MUFFIN =
            ITEMS.register(_muffin_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _muffin_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(2)
                            .saturationModifier(0.5f/2f)
                            .build()
                    )
            ));

    public static final String _melon_pan_id = "melon_pan";
    public static final RegistryObject<Item> MELON_PAN =
            ITEMS.register(_melon_pan_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _melon_pan_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(7)
                            .saturationModifier(0.8f/2f)
                            .build()
                    )
            ));

    public static final String _coffee_eclair_id = "coffee_eclair";
    public static final RegistryObject<Item> COFFEE_ECLAIR =
            ITEMS.register(_coffee_eclair_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_eclair_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(3)
                            .saturationModifier(0.1f/2f)
                            .build()
                    )
            ));

    public static final String _brownie_id = "brownie";
    public static final RegistryObject<Item> BROWNIE =
            ITEMS.register(_brownie_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _brownie_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(2)
                            .saturationModifier(0.4f/2f)
                            .build()
                    )
            ));

    public static final String _tiramisu_slice_id = "tiramisu_slice";
    public static final RegistryObject<Item> TIRAMISU_SLICE =
            ITEMS.register(_tiramisu_slice_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _tiramisu_slice_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(2)
                            .saturationModifier(0.5f/2f)
                            .build()
                    )
            ));

    public static final String _ham_sandwich_id = "ham_sandwich";
    public static final RegistryObject<Item> HAM_SANDWICH =
            ITEMS.register(_ham_sandwich_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _ham_sandwich_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(8)
                            .saturationModifier(0.9f/2f)
                            .build()
                    )
            ));

    public static final String _beef_sandwich_id = "beef_sandwich";
    public static final RegistryObject<Item> BEEF_SANDWICH =
            ITEMS.register(_beef_sandwich_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _beef_sandwich_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(11)
                            .saturationModifier(1.4f/2f)
                            .build()
                    )
            ));

    public static final String _chicken_sandwich_id = "chicken_sandwich";
    public static final RegistryObject<Item> CHICKEN_SANDWICH =
            ITEMS.register(_chicken_sandwich_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _chicken_sandwich_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(9)
                            .saturationModifier(1.2f/2f)
                            .build()
                    )
            ));

    public static final String _croissant_id = "croissant";
    public static final RegistryObject<Item> CROISSANT =
            ITEMS.register(_croissant_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _croissant_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(5)
                            .saturationModifier(1.2f/2f)
                            .build()
                    )
            ));

    public static final String _ham_croissant_id = "ham_croissant";
    public static final RegistryObject<Item> HAM_CROISSANT =
            ITEMS.register(_ham_croissant_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _ham_croissant_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(8)
                            .saturationModifier(0.9f/2f)
                            .build()
                    )
            ));

    public static final String _beef_croissant_id = "beef_croissant";
    public static final RegistryObject<Item> BEEF_CROISSANT =
            ITEMS.register(_beef_croissant_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _beef_croissant_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(11)
                            .saturationModifier(1.4f/2f)
                            .build()
                    )
            ));

    public static final String _chicken_croissant_id = "chicken_croissant";
    public static final RegistryObject<Item> CHICKEN_CROISSANT =
            ITEMS.register(_chicken_croissant_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _chicken_croissant_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(9)
                            .saturationModifier(1.2f/2f)
                            .build()
                    )
            ));

    public static final String _bagel_id = "bagel";
    public static final RegistryObject<Item> BAGEL =
            ITEMS.register(_bagel_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _bagel_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(7)
                            .saturationModifier(1.2f/2f)
                            .build()
                    )
            ));

    public static final String _ham_bagel_id = "ham_bagel";
    public static final RegistryObject<Item> HAM_BAGEL =
            ITEMS.register(_ham_bagel_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _ham_bagel_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(10)
                            .saturationModifier(0.9f/2f)
                            .build()
                    )
            ));

    public static final String _beef_bagel_id = "beef_bagel";
    public static final RegistryObject<Item> BEEF_BAGEL =
            ITEMS.register(_beef_bagel_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _beef_bagel_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(13)
                            .saturationModifier(1.4f/2f)
                            .build()
                    )
            ));

    public static final String _chicken_bagel_id = "chicken_bagel";
    public static final RegistryObject<Item> CHICKEN_BAGEL =
            ITEMS.register(_chicken_bagel_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _chicken_bagel_id)
                    ))
                    .food((new FoodProperties.Builder())
                            .nutrition(11)
                            .saturationModifier(1.2f/2f)
                            .build()
                    )
            ));

    // ############################################### COFFEE BLOCK ITEMS ################################################

    public static final String _brownie_block_item_id = "brownie_block";
    public static final RegistryObject<Item> BROWNIE_BLOCK_ITEM =
            ITEMS.register(_brownie_block_item_id, () -> new BlockItem(
                    BlocksRegistry.BROWNIE_BLOCK.get(),
                    new Item.Properties()
                            .setId(ResourceKey.create(
                                    Registries.ITEM,
                                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _brownie_block_item_id)
                            ))
                            .food((new FoodProperties.Builder())
                                    .nutrition(11)
                                    .saturationModifier(1.2f/2f)
                                    .build()
                            ).component(DataComponents.LORE,new ItemLore(generateComponentTranslatableByItemId(_brownie_block_item_id, true)))
            ));

    public static final String _coffee_machine_item_id = "coffee_machine";
    public static final RegistryObject<Item> COFFEE_MACHINE_ITEM =
            ITEMS.register(_coffee_machine_item_id, () -> new BlockItem(
                    BlocksRegistry.COFFEE_MACHINE.get(),
                    new Item.Properties()
                            .setId(ResourceKey.create(
                                    Registries.ITEM,
                                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_machine_item_id)
                            ))
            ));

    public static final String _coffee_grinder_item_id = "coffee_grinder";
    public static final RegistryObject<Item> COFFEE_GRINDER_ITEM =
            ITEMS.register(_coffee_grinder_item_id, () -> new BlockItem(
                    BlocksRegistry.COFFEE_GRINDER.get(),
                    new Item.Properties()
                            .setId(ResourceKey.create(
                                    Registries.ITEM,
                                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_grinder_item_id)
                            ))
            ));

    public static List<Component> generateComponentTranslatableByItemId(String itemId) {
        return generateComponentTranslatableByItemId(itemId, false);
    }

    public static List<Component> generateComponentTranslatableByItemId(String itemId, boolean isItemDescription) {
        return List.of(Component.translatable("item." + Constants.MOD_ID + "." + itemId + (isItemDescription ? ".desc" : "")));
    }

    public static List<Component> generateMultiLineByItemId(String itemId, int numberOfLines) {
        List<Component> components =  new ArrayList<Component>();
        for(int i = 0; i < numberOfLines; i++)
            components.add(Component.translatable("item." + Constants.MOD_ID + "." + itemId + ".desc." + i));
        return components;
    }

    public static void init() {

    }
}
