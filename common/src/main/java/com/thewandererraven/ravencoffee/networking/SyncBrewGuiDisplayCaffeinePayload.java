package com.thewandererraven.ravencoffee.networking;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncBrewGuiDisplayCaffeinePayload(int caffeinePercentage, boolean isOverloaded) implements CustomPacketPayload {
    public static final Type<SyncBrewGuiDisplayCaffeinePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_gui_display_caffeine_duration"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewGuiDisplayCaffeinePayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeInt(payload.caffeinePercentage());
                        buf.writeBoolean(payload.isOverloaded());
                    },
                    buf -> new SyncBrewGuiDisplayCaffeinePayload(buf.readInt(), buf.readBoolean())
            );

}
