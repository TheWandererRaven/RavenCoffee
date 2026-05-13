package com.thewandererraven.ravencoffee.networking;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SyncBrewGuiDisplayDurationsPayload(int currentEffectRemainingSeconds, int brewTotalRemainingSeconds) implements CustomPacketPayload {
    public static final Type<SyncBrewGuiDisplayDurationsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_gui_display_durations_duration"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewGuiDisplayDurationsPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeInt(payload.currentEffectRemainingSeconds());
                        buf.writeInt(payload.brewTotalRemainingSeconds());
                    },
                    buf -> new SyncBrewGuiDisplayDurationsPayload(buf.readInt(), buf.readInt())
            );

}
