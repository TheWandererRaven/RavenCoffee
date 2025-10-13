package com.thewandererraven.ravencoffee.screen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.menu.CoffeeGrinderMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CoffeeGrinderScreen extends AbstractContainerScreen<CoffeeGrinderMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/menus/coffee_grinder.png");

    public CoffeeGrinderScreen(CoffeeGrinderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        titleLabelY = 10;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        Minecraft.getInstance().getTextureManager().release(TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }
}
