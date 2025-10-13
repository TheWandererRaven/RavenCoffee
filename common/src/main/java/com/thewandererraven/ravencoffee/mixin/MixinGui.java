package com.thewandererraven.ravencoffee.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffect;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffectInstance;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(Gui.class)
public class MixinGui {
    @Shadow
    private Minecraft minecraft;
    private static final ResourceLocation MULTI_EFFECT_BACKGROUND_AMBIENT_SPRITE = ResourceLocation.withDefaultNamespace("hud/effect_background_ambient");
    private static final ResourceLocation MULTI_EFFECT_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("hud/effect_background");


    @Inject(
            method = "renderEffects",
            at = @At("TAIL")
    )
    private void ravencoffee$renderMultiEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        MultiEffectInstance currentEffect = null;//this.minecraft.player.getCurrentMultiEffect();
        if (currentEffect != null && (this.minecraft.screen == null || !this.minecraft.screen.showsActiveEffects())) {
            int i = 0;
            int j = 0;
            int k = guiGraphics.guiWidth() - 25;
            int l = 1 + 26;
            MobEffectTextureManager mobeffecttexturemanager = this.minecraft.getMobEffectTextures();
            //List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());

                //MultiEffect baseEffect = currentEffect.multiEffect;
                    float f = 1.0F;
                    if (currentEffect.isAmbient()) {
                        guiGraphics.blitSprite(RenderType::guiTextured, MULTI_EFFECT_BACKGROUND_AMBIENT_SPRITE, k, l, 24, 24);
                    } else {
                        guiGraphics.blitSprite(RenderType::guiTextured, MULTI_EFFECT_BACKGROUND_SPRITE, k, l, 24, 24);
                        if (currentEffect.endsWithin(200)) {
                            int i1 = currentEffect.getRemainingDuration();
                            int j1 = 10 - i1 / 20;
                            f = Mth.clamp((float)i1 / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((float)i1 * (float)Math.PI / 5.0F) * Mth.clamp((float)j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                            f = Mth.clamp(f, 0.0F, 1.0F);
                        }
                    ResourceLocation effectIconLocation = currentEffect.getIconLocation();
                    float finalF = f;
                        int i2 = ARGB.white(finalF);
                        guiGraphics.blitSprite(RenderType::guiTextured, effectIconLocation, k + 3, l + 3, 18, 18, i2);
                }
        }
    }
}
