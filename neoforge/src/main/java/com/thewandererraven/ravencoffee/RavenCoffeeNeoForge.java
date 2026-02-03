package com.thewandererraven.ravencoffee;


import com.thewandererraven.ravencoffee.effect.breweffect.CoffeeBrewEffectInstance;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffectsRegistry;
import com.thewandererraven.ravencoffee.menu.MenusRegistry;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewPayload;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import com.thewandererraven.ravencoffee.screen.CoffeeGrinderScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
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
        }
        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playBidirectional(
                    SyncBrewPayload.TYPE,
                    SyncBrewPayload.STREAM_CODEC,
                    new DirectionalPayloadHandler<>(
                            // CLIENT
                            (payload, context) -> {
                                Minecraft client = Minecraft.getInstance();
                                client.execute(() -> {
                                    Player player = client.player;
                                    IBrewManagerHolder holder = (IBrewManagerHolder) player;
                                    if(payload.brewId() != null) {
                                        var holderEffect = MultiEffectsRegistry.BREW_EFFECTS.getEntries().stream()
                                                .filter(entry -> entry.get().getId().equals(payload.brewId()))
                                                .findFirst().orElse(null);
                                        if (holderEffect == null) return;

                                        CoffeeBrewEffectInstance instance = new CoffeeBrewEffectInstance(holderEffect.asHolder());
                                        holder.ravencoffee$getBrewEffectManager()
                                                .setClientEffect(instance, payload.duration());
                                    }
                                });
                            },
                            // SERVER
                            (payload, context) -> {}
                    )
            );
            registrar.commonToClient(
                    SyncBrewManagerPayload.TYPE,
                    SyncBrewManagerPayload.STREAM_CODEC,
                    new MainThreadPayloadHandler<>(
                            (payload, context) -> {
                                Minecraft client = Minecraft.getInstance();
                                client.execute(() -> {
                                    Player player = client.player;
                                    IBrewManagerHolder holder = (IBrewManagerHolder) player;
                                    if(payload.currentCaffeine() >= 0) {
                                        holder.ravencoffee$getBrewEffectManager().setCurrentCaffeine(payload.currentCaffeine());
                                    }
                                    holder.ravencoffee$getBrewEffectManager().setOverloaded(payload.isOverloaded());
                                });
                            }
                    )
            );
        }
    }
}