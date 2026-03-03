package com.thewandererraven.ravencoffee.menu;

import com.thewandererraven.ravencoffee.menu.slots.CoffeeBrewingStationIngredientSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CoffeeBrewingStationMenu extends AbstractContainerMenu {
    private static final int BASE_INGREDIENTS_SLOTS_COUNT = 1;
    public static final int INGREDIENTS_SLOTS_COUNT = 3;
    private final SimpleContainer resultContainer = new SimpleContainer(1);
    private final SimpleContainer baseIngredientContainer = new SimpleContainer(BASE_INGREDIENTS_SLOTS_COUNT);
    private final SimpleContainer mugsContainer = new SimpleContainer(1);
    private final SimpleContainer ingredientsContainer = new SimpleContainer(INGREDIENTS_SLOTS_COUNT);

    public CoffeeBrewingStationMenu(int containerId, Inventory playerInventory) {
        super(MenusRegistry.COFFEE_BREWING_STATION.get(), containerId);
        //this.player = playerInventory.player;

        addSlot(new Slot(this.resultContainer, 0, RESULT_SLOT_POS_X, RESULT_SLOT_POS_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addSlot(new Slot(this.baseIngredientContainer, 0, BASE_INGREDIENT_SLOT_POS_X,  BASE_INGREDIENT_SLOT_POS_Y));
        addSlot(new Slot(this.mugsContainer, 0, MUGS_SLOT_POS_X,  MUGS_SLOT_POS_Y));
        for(int i = 0; i < INGREDIENTS_SLOTS_COUNT; i++)
            addSlot(new CoffeeBrewingStationIngredientSlot(
                    this,
                    this.ingredientsContainer,
                    i,
                    CoffeeBrewingStationMenu.INGREDIENT_SLOT_POS_X + ((CoffeeBrewingStationMenu.INGREDIENT_SLOT_X_SPACING + CoffeeBrewingStationMenu.INGREDIENT_SLOT_WIDTH) * i),
                    INGREDIENT_SLOT_POS_Y
            ));

        this.updateSlotsVisibility();

        addStandardInventorySlots(playerInventory, PLAYER_INVENTORY_XPOS, PLAYER_INVENTORY_YPOS);
    }

    public void updateSlotsVisibility() {
        for(int i = this.INGREDIENTS_SLOTS_START_INDEX; i < (this.INGREDIENTS_SLOTS_START_INDEX + INGREDIENTS_SLOTS_COUNT - 1); i++) {
            CoffeeBrewingStationIngredientSlot currentSlot = ((CoffeeBrewingStationIngredientSlot) this.slots.get(i));
            CoffeeBrewingStationIngredientSlot nextSlot = ((CoffeeBrewingStationIngredientSlot) this.slots.get(i + 1));
            if (!currentSlot.getItem().isEmpty())
                nextSlot.show();
            else
                nextSlot.hide();
        }
    }

    @Override
    public void slotsChanged(Container container) {
        if(container == this.ingredientsContainer) {
            this.reorganizeSlots();
            this.updateSlotsVisibility();
        }
    }

    public void reorganizeSlots() {
        for(int i = this.INGREDIENTS_SLOTS_START_INDEX; i < (this.INGREDIENTS_SLOTS_START_INDEX + INGREDIENTS_SLOTS_COUNT); i++) {
            Slot currentSlot = this.slots.get(i);
            if(currentSlot.getItem().isEmpty()) {
                for(int j = i + 1; j < (this.INGREDIENTS_SLOTS_START_INDEX + INGREDIENTS_SLOTS_COUNT); j++) {
                    Slot subSlot = this.slots.get(j);
                    if(!subSlot.getItem().isEmpty()) {
                        currentSlot.set(subSlot.remove(subSlot.getItem().getCount()));
                        break;
                    }
                }
            }
        }
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
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // ########################### INDEXES ###########################

    private final int RESULT_SLOT_INDEX = 0;
    private final int BASE_INGREDIENTS_SLOTS_START_INDEX = 1;
    private final int CUPS_START_INDEX = 2;
    private final int INGREDIENTS_SLOTS_START_INDEX = 3;

    private final int PLAYER_INV_START_INDEX = 5;
    private final int PLAYER_HOTBAR_START_INDEX = PLAYER_INV_START_INDEX + 27;
    private final int PLAYER_HOTBAR_END_INDEX = PLAYER_HOTBAR_START_INDEX + 9;

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
