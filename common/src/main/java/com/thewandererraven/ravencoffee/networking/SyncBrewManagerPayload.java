package com.thewandererraven.ravencoffee.networking;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncBrewManagerPayload(int currentCaffeine, boolean isOverloaded) implements CustomPacketPayload {
    public static final Type<SyncBrewManagerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_manager"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewManagerPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeInt(payload.currentCaffeine());
                        buf.writeBoolean(payload.isOverloaded());
                    },
                    buf -> new SyncBrewManagerPayload(buf.readInt(), buf.readBoolean())
            );

}
