package com.thewandererraven.ravencoffee.menu;

import com.thewandererraven.ravenbrewslib.brew.data.BrewBase;
import com.thewandererraven.ravenbrewslib.brew.data.BrewEffectDefinition;
import com.thewandererraven.ravenbrewslib.brew.data.BrewIngredient;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.datacomponents.*;
import com.thewandererraven.ravencoffee.item.BrewItem;
import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import com.thewandererraven.ravencoffee.menu.slots.CoffeeBrewingStationIngredientSlot;
import com.thewandererraven.ravencoffee.menu.slots.CoffeeBrewingStationResultSlot;
import com.thewandererraven.ravencoffee.recipe.brewing.BrewBaseRegistry;
import com.thewandererraven.ravencoffee.recipe.brewing.BrewIngredientRegistry;
import com.thewandererraven.ravencoffee.recipe.brewing.BrewVariantRegistry;
import com.thewandererraven.ravencoffee.util.RavenCoffeeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoffeeBrewingStationMenu extends AbstractContainerMenu {
    private static final int BASE_INGREDIENTS_SLOTS_COUNT = 1;
    public static final int INGREDIENT_SLOTS_COUNT = 3;
    private final SimpleContainer resultContainer = new SimpleContainer(1);
    private final SimpleContainer baseIngredientContainer = new SimpleContainer(BASE_INGREDIENTS_SLOTS_COUNT);
    private final SimpleContainer mugsContainer = new SimpleContainer(1);
    private final SimpleContainer ingredientsContainer = new SimpleContainer(INGREDIENT_SLOTS_COUNT);

    public CoffeeBrewingStationMenu(int containerId, Inventory playerInventory) {
        super(MenusRegistry.COFFEE_BREWING_STATION.get(), containerId);

        addSlot(new CoffeeBrewingStationResultSlot(this.resultContainer, 0, RESULT_SLOT_POS_X, RESULT_SLOT_POS_Y, this) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addSlot(new Slot(this.baseIngredientContainer, 0, BASE_INGREDIENT_SLOT_POS_X,  BASE_INGREDIENT_SLOT_POS_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(RavenCoffeeTags.Items.COFFEE_BREW_BASE);
            }
        });
        addSlot(new Slot(this.mugsContainer, 0, MUGS_SLOT_POS_X,  MUGS_SLOT_POS_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(RavenCoffeeTags.Items.COFFEE_BREW_CONTAINER);
            }
        });
        for(int i = 0; i < INGREDIENT_SLOTS_COUNT; i++)
            addSlot(new CoffeeBrewingStationIngredientSlot(
                    this,
                    this.ingredientsContainer,
                    i,
                    CoffeeBrewingStationMenu.INGREDIENT_SLOT_POS_X + ((CoffeeBrewingStationMenu.INGREDIENT_SLOT_X_SPACING + CoffeeBrewingStationMenu.INGREDIENT_SLOT_WIDTH) * i),
                    INGREDIENT_SLOT_POS_Y
            ));

        this.updateSlotsVisibility();

        addStandardInventorySlots(playerInventory, PLAYER_INVENTORY_XPOS, PLAYER_INVENTORY_YPOS);
        this.ingredientsContainer.addListener(this::slotsChanged);
        this.baseIngredientContainer.addListener(this::slotsChanged);
        this.mugsContainer.addListener(this::slotsChanged);
    }

    @Override
    public void removed(Player player) {
        if (player instanceof ServerPlayer) {
            this.clearContainer(player, this.mugsContainer);
            this.clearContainer(player, this.baseIngredientContainer);
            this.clearContainer(player, this.ingredientsContainer);
        }
    }

    @Override
    public void slotsChanged(Container container) {
        this.updateEverything();
    }

    void updateEverything() {
        this.reorganizeSlots();
        this.updateSlotsVisibility();
        this.updateResultSlot();
    }

    public void reorganizeSlots() {
        for(int i = this.INGREDIENTS_SLOTS_START_INDEX; i < (this.INGREDIENTS_SLOTS_START_INDEX + INGREDIENT_SLOTS_COUNT); i++) {
            Slot currentSlot = this.slots.get(i);
            if(currentSlot.getItem().isEmpty()) {
                for(int j = i + 1; j < (this.INGREDIENTS_SLOTS_START_INDEX + INGREDIENT_SLOTS_COUNT); j++) {
                    Slot subSlot = this.slots.get(j);
                    if(!subSlot.getItem().isEmpty()) {
                        currentSlot.set(subSlot.remove(subSlot.getItem().getCount()));
                        break;
                    }
                }
            }
        }
    }

    public void updateSlotsVisibility() {
        for(int i = this.INGREDIENTS_SLOTS_START_INDEX; i < (this.INGREDIENTS_SLOTS_START_INDEX + INGREDIENT_SLOTS_COUNT - 1); i++) {
            CoffeeBrewingStationIngredientSlot currentSlot = ((CoffeeBrewingStationIngredientSlot) this.slots.get(i));
            CoffeeBrewingStationIngredientSlot nextSlot = ((CoffeeBrewingStationIngredientSlot) this.slots.get(i + 1));
            if (!currentSlot.getItem().isEmpty())
                nextSlot.show();
            else
                nextSlot.hide();
        }
    }

    BrewIngredient findIngredientData(Item item) {
        return BrewIngredientRegistry.get(item);
    }

    BrewBase findBaseData(Item item) {
        return BrewBaseRegistry.get(item);
    }

    ResourceLocation findBrewVariant(List<Item> items) {
        return BrewVariantRegistry.get(items).orElse(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "default"));
    }

    public void updateResultSlot() {
        ItemStack resultStack = ItemStack.EMPTY;
        if(!this.isMugsSlotEmpty() && !this.baseIngredientContainer.isEmpty()) {
            int ingredientsTotalCaffeine = 0;
            int minStackSize = 64;
            List<BrewEffectDefinition.Builder> brewEffects = new ArrayList<>();
            ResourceLocation brewVariant = findBrewVariant(getIngredientItems());

            ItemStack mugStack = this.mugsContainer.getItem(0);
            if(mugStack.getCount() < minStackSize)
                minStackSize = mugStack.getCount();

            ItemStack baseStack = this.baseIngredientContainer.getItem(0);
            if(baseStack.getCount() < minStackSize)
                minStackSize = baseStack.getCount();

            for (ItemStack ingStack : this.ingredientsContainer) {
                if (ingStack.isEmpty())
                    continue;

                BrewIngredient data = this.findIngredientData(ingStack.getItem());
                if (data == null)
                    continue;
                if (data.item().equals(Items.AIR))
                    continue;

                ingredientsTotalCaffeine += data.caffeineDelta();
                if(ingStack.getCount() < minStackSize)
                    minStackSize = ingStack.getCount();

                for (BrewEffectDefinition effData : data.effects()) {
                    boolean isDuplicateEffect = false;
                    for (BrewEffectDefinition.Builder eff : brewEffects) {
                        if (effData.id().equals(eff.id)) {
                            if (effData.duration() > 0)
                                eff.addDuration(effData.duration() / 2);
                            if (effData.mainValue() > 0)
                                eff.addMainValue(effData.mainValue() / 2);
                            if (effData.secondaryValue() > 0)
                                eff.addSecondaryValue(effData.secondaryValue() / 2);
                            isDuplicateEffect = true;
                            break;
                        }
                    }
                    if (!isDuplicateEffect) {
                        brewEffects.add(new BrewEffectDefinition.Builder(
                                effData.id(),
                                effData.duration(),
                                effData.mainValue(),
                                effData.secondaryValue()
                        ));
                    }
                }

            }

            if(this.ingredientsContainer.isEmpty()) {
                brewEffects.addAll(BrewEffectDefinition.getListOfDefaultEffects());
            }

            BrewBase baseData = findBaseData(baseStack.getItem());
            if (baseData != null)
                if (!baseData.item().equals(Items.AIR)) {
                    resultStack = new ItemStack(GeneralItemsRegistry.COFFEE_BREW.get(), minStackSize);
                    resultStack.set(DataComponentTypes.COFFEE_BREW.get(), new CoffeeBrewData(
                            brewVariant,
                            (int) Math.ceil((baseData.caffeineBase() + (ingredientsTotalCaffeine * baseData.caffeineMultiplier())) * 20),
                            brewEffects.stream().map(eff ->
                                    eff.scaleDuration(baseData.durationMultiplier())
                                            .scaleMainValue(baseData.effectValuesMultiplier())
                                            .scaleSecondaryValue(baseData.effectValuesMultiplier())
                                            .build()
                            ).toList()
                    ));
                }
        }
        this.resultContainer.setItem(0, resultStack);
    }

    public void consumeIngredients(int count) {
        this.mugsContainer.getItem(0).shrink(count);
        this.baseIngredientContainer.getItem(0).shrink(count);
        for(int i = 0; i < this.ingredientsContainer.getContainerSize(); i++) {
            ItemStack stack = this.ingredientsContainer.getItem(i);
            if(stack.is(Items.MILK_BUCKET)) {
                this.ingredientsContainer.setItem(i, new ItemStack(Items.BUCKET));
            } else {
                stack.shrink(count);
            }
        }
    }

    List<Item> getIngredientItems() {
        return this.ingredientsContainer.getItems().stream().map(ItemStack::getItem).filter(item -> !item.equals(Items.AIR)).toList();
    }

    public int getCurrentIngredientsCount() {
        int count = 0;
        for(ItemStack stack: this.ingredientsContainer) {
            if(!stack.isEmpty())
                count++;
        }
        return count;
    }

    public boolean isMugsSlotEmpty() {
        return this.mugsContainer.isEmpty();
    }

    public boolean isResultSlotEmpty() {
        return this.resultContainer.isEmpty();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int originSlotIndex) {
        if(this.slots.get(originSlotIndex).getItem().isEmpty())
            return ItemStack.EMPTY;

        boolean movedSuccessfully = true;

        ItemStack stackToMove = this.slots.get(originSlotIndex).getItem();
        int countToMove = stackToMove.getCount();
        // ------------------------------------------------------ from the RESULT SLOT
        if(originSlotIndex == RESULT_SLOT_INDEX) {
            if(!this.moveItemStackTo(stackToMove, PLAYER_HOTBAR_START_INDEX, PLAYER_HOTBAR_END_INDEX, true))
                if(!this.moveItemStackTo(stackToMove, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_START_INDEX, true))
                    movedSuccessfully = false;
            if(movedSuccessfully)
                consumeIngredients(countToMove);
        }
        // ------------------------------------------------------ from any CRAFTING SLOTS
        else if(originSlotIndex < PLAYER_INV_START_INDEX) {
            if(!this.moveItemStackTo(stackToMove, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_START_INDEX, false))
                if(!this.moveItemStackTo(stackToMove, PLAYER_HOTBAR_START_INDEX, PLAYER_HOTBAR_END_INDEX, false))
                    movedSuccessfully = false;
        }
        // ------------------------------------------------------ from PLAYER INVENTORY
        else {
            if (!this.moveItemStackTo(stackToMove, BASE_INGREDIENTS_SLOTS_START_INDEX, CUPS_SLOT_INDEX, false))
                if (!this.moveItemStackTo(stackToMove, CUPS_SLOT_INDEX, INGREDIENTS_SLOTS_START_INDEX, false))
                    if (!this.moveItemStackTo(stackToMove, INGREDIENTS_SLOTS_START_INDEX, PLAYER_INV_START_INDEX, false))
                        if (originSlotIndex < PLAYER_HOTBAR_START_INDEX) { // Aka: it's in the player inventory (not hotbar)
                            if (!this.moveItemStackTo(stackToMove, PLAYER_HOTBAR_START_INDEX, PLAYER_HOTBAR_END_INDEX, false))
                                movedSuccessfully = false;
                        } else { // the only remaining slots are the ones from the hotbar
                            if (!this.moveItemStackTo(stackToMove, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_START_INDEX, false))
                                movedSuccessfully = false;
                        }

        }

        if(movedSuccessfully) {
            this.updateEverything();
            return stackToMove;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // ########################### INDEXES ###########################

    private static final int RESULT_SLOT_INDEX = 0;
    private static final int BASE_INGREDIENTS_SLOTS_START_INDEX = 1;
    private static final int CUPS_SLOT_INDEX = 2;
    private static final int INGREDIENTS_SLOTS_START_INDEX = 3;

    private static final int PLAYER_INV_START_INDEX = 6;
    private static final int PLAYER_HOTBAR_START_INDEX = PLAYER_INV_START_INDEX + 27;
    private static final int PLAYER_HOTBAR_END_INDEX = PLAYER_HOTBAR_START_INDEX + 9;

    // ########################### POSITIONS ###########################

    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 84;

    public static final int RESULT_SLOT_POS_X = 98;
    public static final int RESULT_SLOT_POS_Y = 52;

    public static final int INGREDIENT_SLOT_POS_X = 78;
    public static final int INGREDIENT_SLOT_X_SPACING = 4;
    public static final int INGREDIENT_SLOT_POS_Y = 24;
    public static final int INGREDIENT_SLOT_WIDTH = 16;

    public static final int BASE_INGREDIENT_SLOT_POS_X = 45;
    public static final int BASE_INGREDIENT_SLOT_POS_Y = 24;

    public static final int MUGS_SLOT_POS_X = 45;
    public static final int MUGS_SLOT_POS_Y = 52;
}
