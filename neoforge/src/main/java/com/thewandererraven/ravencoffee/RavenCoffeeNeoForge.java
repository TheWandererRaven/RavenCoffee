package com.thewandererraven.ravencoffee;


import com.thewandererraven.ravencoffee.effect.breweffect.DefaultCoffeeBrewEffectsManager;
import com.thewandererraven.ravencoffee.item.properties.BrewVariantProperty;
import com.thewandererraven.ravencoffee.menu.MenusRegistry;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayDurationsPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayIconsPayload;
import com.thewandererraven.ravencoffee.platform.services.IBrewGuiDisplayHolder;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import com.thewandererraven.ravencoffee.screen.CoffeeBrewingStationScreen;
import com.thewandererraven.ravencoffee.screen.CoffeeGrinderScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(Constants.MOD_ID)
public class RavenCoffeeNeoForge {

    public RavenCoffeeNeoForge(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        RavenCoffeeCommon.init();
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(MenusRegistry.COFFEE_GRINDER.get(), CoffeeGrinderScreen::new);
            event.register(MenusRegistry.COFFEE_BREWING_STATION.get(), CoffeeBrewingStationScreen::new);
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");

            registrar.playToClient(
                    SyncBrewGuiDisplayCaffeinePayload.TYPE,
                    SyncBrewGuiDisplayCaffeinePayload.STREAM_CODEC,
                    (payload, context) -> {
                        Minecraft client = Minecraft.getInstance();
                        IBrewGuiDisplayHolder holder = (IBrewGuiDisplayHolder) client.gui;
                        if(payload.caffeinePercentage() >= 0) {
                            holder.ravencoffee$getBrewGuiDisplayHolder().setCaffeinePercentage(payload.caffeinePercentage());
                        }
                        holder.ravencoffee$getBrewGuiDisplayHolder().setCaffeineOverload(payload.isOverloaded());
                    }
            );
            registrar.playToClient(
                    SyncBrewGuiDisplayDurationsPayload.TYPE,
                    SyncBrewGuiDisplayDurationsPayload.STREAM_CODEC,
                    (payload, context) -> {
                        Minecraft client = Minecraft.getInstance();
                        IBrewGuiDisplayHolder holder = (IBrewGuiDisplayHolder) client.gui;
                        holder.ravencoffee$getBrewGuiDisplayHolder().setCurrentEffectDurationSeconds(payload.currentEffectRemainingSeconds());
                        holder.ravencoffee$getBrewGuiDisplayHolder().setBrewTotalDurationSeconds(payload.brewTotalRemainingSeconds());
                    }
            );
            registrar.playToClient(
                    SyncBrewGuiDisplayIconsPayload.TYPE,
                    SyncBrewGuiDisplayIconsPayload.STREAM_CODEC,
                    (payload, context) -> {
                        Minecraft client = Minecraft.getInstance();
                        IBrewGuiDisplayHolder holder = (IBrewGuiDisplayHolder) client.gui;
                        holder.ravencoffee$getBrewGuiDisplayHolder().setEffectIcons(payload.effectIcons());
                    }
            );
        }

        @SubscribeEvent
        public static void registerSelectItemModelProperties(RegisterSelectItemModelPropertyEvent event) {
            event.register(
                    // The name to reference as the type
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brew_variant"),
                    // The map codec
                    BrewVariantProperty.TYPE
            );
        }
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientNeoForgeEvents {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if(event.getEntity() instanceof ServerPlayer player) {
                DefaultCoffeeBrewEffectsManager manager = ((IBrewManagerHolder) player).ravencoffee$getBrewEffectManager();
                manager.sendAllInfoToClient();
            }
        }
    }
}