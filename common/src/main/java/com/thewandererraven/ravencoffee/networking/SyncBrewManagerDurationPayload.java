package com.thewandererraven.ravencoffee.networking;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncBrewManagerDurationPayload(int currentEffectRemainingTicks, int totalEffectRemainingTicks) implements CustomPacketPayload {
    public static final Type<SyncBrewManagerDurationPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_manager_duration"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewManagerDurationPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeInt(payload.currentEffectRemainingTicks());
                        buf.writeInt(payload.totalEffectRemainingTicks());
                    },
                    buf -> new SyncBrewManagerDurationPayload(buf.readInt(), buf.readInt())
            );

}
