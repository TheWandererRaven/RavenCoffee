package com.thewandererraven.ravencoffee.menu;

import com.thewandererraven.ravencoffee.recipe.CoffeeGrindingRecipe;
import com.thewandererraven.ravencoffee.recipe.RecipesRegistry;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Optional;

public class CoffeeGrinderMenu extends AbstractContainerMenu {
    private final CoffeeGrinderIngredientsContainer ingredientsContainer = new CoffeeGrinderIngredientsContainer(this);
    private final CoffeeGrinderResultContainer resultContainer = new CoffeeGrinderResultContainer(this);
    private final Player player;

    private final int INPUT_SLOT_INDEX = 0;
    private final int OUTPUT_SLOT_INDEX = 2;
    private final int GRINDER_SLOTS_COUNT = 3;

    private final int PLAYER_INV_START_INDEX = GRINDER_SLOTS_COUNT;
    private final int PLAYER_HOTBAR_START_INDEX = GRINDER_SLOTS_COUNT + 27;
    private final int PLAYER_HOTBAR_END_INDEX = PLAYER_HOTBAR_START_INDEX + 9;

    // ########################### POSITIONS ###########################

    public static final int PLAYER_HOTBAR_XPOS = 8;
    public static final int PLAYER_HOTBAR_YPOS = 142;
    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 84;

    public static final int OUTPUT_SLOT_POS_X = 116;
    public static final int OUTPUT_SLOT_POS_Y = 35;

    public static final int INPUT_SLOT_POS_X = 56;
    public static final int INPUT_SLOT_Y_SPACING = 19;
    public static final int INPUT_SLOT_POS_Y = 25;

    public static final int SLOT_X_SPACING = 18;
    public static final int SLOT_Y_SPACING = 18;

    public CoffeeGrinderMenu(int syncId, Inventory playerInventory) {
        super(MenusRegistry.COFFEE_GRINDER.get(), syncId);
        this.player = playerInventory.player;
        //this.ingredientsContainer.addListener(this::slotsChanged);

        // Add the tile input containers to the gui
        for(int i = 0; i < 2; i++)
            addSlot(new Slot(this.ingredientsContainer, i, INPUT_SLOT_POS_X,  INPUT_SLOT_POS_Y + (i * INPUT_SLOT_Y_SPACING)));

        // Add the tile output containers to the gui
        addSlot(new Slot(this.resultContainer, OUTPUT_SLOT_INDEX, OUTPUT_SLOT_POS_X,  OUTPUT_SLOT_POS_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // TODO: implement addStandardInventorySlots from AbstractContainerMenu to align this to the OG code

        // Add the rest of the players inventory to the gui
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                int slotNumber = (9 * (y + 1)) + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new Slot(playerInventory, slotNumber,  xpos, ypos));
            }
        }

        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, PLAYER_HOTBAR_XPOS + SLOT_X_SPACING * x, PLAYER_HOTBAR_YPOS));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            // If selected item is in output...
            if (index == OUTPUT_SLOT_INDEX) {
                // ...move to rightmost hotbar slot...
                if (!this.moveItemStackTo(itemstack1, PLAYER_HOTBAR_START_INDEX, PLAYER_HOTBAR_END_INDEX, true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            }
            // If selected item is in vanilla slots...
            else if (index >= PLAYER_INV_START_INDEX && index < PLAYER_HOTBAR_END_INDEX) {
                // ...move to input slots...
                if (!this.moveItemStackTo(itemstack1, INPUT_SLOT_INDEX, OUTPUT_SLOT_INDEX, false))
                    // ...if input slots are full, and if selected slot is from player inventory
                    if(index < PLAYER_HOTBAR_START_INDEX) {
                        // ...move to hotbar...
                        if (!this.moveItemStackTo(itemstack1, PLAYER_HOTBAR_START_INDEX, PLAYER_HOTBAR_END_INDEX, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    // ...otherwise, selected slot is from hotbar so, move to inventory
                    else if (!this.moveItemStackTo(itemstack1, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_START_INDEX, false))
                        return ItemStack.EMPTY;
            }
            // If selected item is in input slots...
            // ...move to Player vanilla slots (first to player inventory, then to hotbar)
            else if (!this.moveItemStackTo(itemstack1, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_END_INDEX, false))
                return ItemStack.EMPTY;

            if (itemstack1.isEmpty()) {
                slot.safeInsert(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.getItem();
            slot.onTake(player, itemstack1);
            if (index == OUTPUT_SLOT_INDEX)
                player.drop(itemstack2, false);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            this.clearContainer(player, this.ingredientsContainer);
        }
        //this.ingredientsContainer.removeListener(this::slotsChanged);
    }

    @Override
    public void slotsChanged(Container container) {
        //super.slotsChanged(container);
        if(container == this.ingredientsContainer)
            this.ingredientsSlotsChanged();
    }

    public void craftedOutput(int craftedCount) {
        for(int i = 0; i < this.ingredientsContainer.size(); i++) {
            if(!this.ingredientsContainer.getItem(i).isEmpty()) {
                this.ingredientsContainer.getItem(i).shrink(craftedCount);
                if(this.ingredientsContainer.getItem(i).getCount() <= 0)
                    this.ingredientsContainer.setItem(i, ItemStack.EMPTY);
//                else
//                    this.ingredientsSlotsChanged();
            }
        }
    }

    private void updateRecipe() {
        
    }

    private void ingredientsSlotsChanged() {
        Level level = player.level();
        if(!level.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;

            RecipeHolder<?> foundRecipe = null;
            if(this.resultContainer.getRecipeUsed() == null) {
                Optional<RecipeHolder<CoffeeGrindingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipesRegistry.COFFEE_GRINDING.get(), this.ingredientsContainer, level);
                if(optional.isPresent())
                    foundRecipe = optional.orElse(null);
            }

            if(foundRecipe == null)
                this.resultContainer.setRecipeUsed(null);
            else if (this.resultContainer.setRecipeUsed(serverplayer, foundRecipe)) {
                ItemStack itemstack1 = ((CoffeeGrindingRecipe) foundRecipe.value()).assemble(this.ingredientsContainer, level.registryAccess());
                if (itemstack1.isItemEnabled(level.enabledFeatures())) {
                    itemstack = itemstack1;
                }
            }
            this.resultContainer.setItem(0, itemstack);
            setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), 0, itemstack));
        }
    }

}
