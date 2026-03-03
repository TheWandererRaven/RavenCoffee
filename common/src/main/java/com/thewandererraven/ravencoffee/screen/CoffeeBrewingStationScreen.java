package com.thewandererraven.ravencoffee.screen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.menu.CoffeeBrewingStationMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CoffeeBrewingStationScreen extends AbstractContainerScreen<CoffeeBrewingStationMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/menus/coffee_brewing_station.png");

    private static final int PROGRESS_INDICATOR_X = 102;
    private static final int PROGRESS_INDICATOR_Y = 16;

    private static final int genericSlotWidth = 18;
    private static final int genericSlotHeight = 18;

    private static final int genericSlotIconWidth = genericSlotWidth - 2;
    private static final int genericSlotIconHeight = genericSlotHeight - 2;

    public CoffeeBrewingStationScreen(CoffeeBrewingStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        titleLabelY = 10;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        Minecraft.getInstance().getTextureManager().release(TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        // ========== BACKGROUND TEXTURE
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        // ========== INGREDIENT SLOTS
        int ingredientsCount = this.getMenu().getCurrentIngredientsCount();
        for(int j = 1; j < ingredientsCount + 1 && j < CoffeeBrewingStationMenu.INGREDIENTS_SLOTS_COUNT; j++) {
            guiGraphics.blit(RenderType::guiTextured, TEXTURE,
                    (x + CoffeeBrewingStationMenu.INGREDIENT_SLOT_POS_X + ((CoffeeBrewingStationMenu.INGREDIENT_SLOT_X_SPACING + CoffeeBrewingStationMenu.INGREDIENT_SLOT_WIDTH) * j)) - 1,
                    (y + CoffeeBrewingStationMenu.INGREDIENT_SLOT_POS_Y) - 1,
                    imageWidth, 64,
                    genericSlotWidth, genericSlotHeight,
                    256, 256
            );
        }
        // ========== INGREDIENT SLOTS + button
        if(ingredientsCount < CoffeeBrewingStationMenu.INGREDIENTS_SLOTS_COUNT - 1)
            guiGraphics.blit(RenderType::guiTextured, TEXTURE,
                    x + CoffeeBrewingStationMenu.INGREDIENT_SLOT_POS_X + ((CoffeeBrewingStationMenu.INGREDIENT_SLOT_X_SPACING + CoffeeBrewingStationMenu.INGREDIENT_SLOT_WIDTH) * (ingredientsCount + 1)),
                    y + CoffeeBrewingStationMenu.INGREDIENT_SLOT_POS_Y,
                    imageWidth + (genericSlotIconWidth * 2), 0.0f,
                    genericSlotIconWidth, genericSlotIconHeight,
                    256, 256
            );
        // ========== MUGS INDICATOR
        if(this.getMenu().isMugsSlotEmpty())
            guiGraphics.blit(RenderType::guiTextured, TEXTURE,
                    x + CoffeeBrewingStationMenu.MUGS_SLOT_POS_X,
                    y + CoffeeBrewingStationMenu.MUGS_SLOT_POS_Y,
                    imageWidth, 0,
                    genericSlotIconWidth, genericSlotIconHeight,
                    256, 256
            );
        // ========== RESULT BREW INDICATOR
        if(this.getMenu().isResultSlotEmpty())
            guiGraphics.blit(RenderType::guiTextured, TEXTURE,
                    x + CoffeeBrewingStationMenu.RESULT_SLOT_POS_X,
                    y + CoffeeBrewingStationMenu.RESULT_SLOT_POS_Y,
                    imageWidth + genericSlotIconWidth, 0,
                    genericSlotIconWidth, genericSlotIconHeight,
                    256, 256
            );
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }
}
