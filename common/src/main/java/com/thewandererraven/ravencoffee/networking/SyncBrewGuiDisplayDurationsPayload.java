package com.thewandererraven.ravencoffee.networking;

import com.mojang.serialization.Codec;
import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record SyncBrewGuiDisplayDurationsPayload(int totalDuration, List<Integer> activeEffectsDurations) implements CustomPacketPayload {
    public static final Type<SyncBrewGuiDisplayDurationsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_gui_display_durations"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewGuiDisplayDurationsPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    SyncBrewGuiDisplayDurationsPayload::totalDuration,
                    ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.INT),
                    SyncBrewGuiDisplayDurationsPayload::activeEffectsDurations,
                    SyncBrewGuiDisplayDurationsPayload::new
            );

}
