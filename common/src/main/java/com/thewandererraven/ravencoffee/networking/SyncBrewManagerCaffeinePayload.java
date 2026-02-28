package com.thewandererraven.ravencoffee.networking;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncBrewManagerCaffeinePayload(int currentCaffeine, boolean isOverloaded) implements CustomPacketPayload {
    public static final Type<SyncBrewManagerCaffeinePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_manager_caffeine"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewManagerCaffeinePayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeInt(payload.currentCaffeine());
                        buf.writeBoolean(payload.isOverloaded());
                    },
                    buf -> new SyncBrewManagerCaffeinePayload(buf.readInt(), buf.readBoolean())
            );
}
