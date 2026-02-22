package com.thewandererraven.ravencoffee.networking;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record SyncBrewManagerIconsPayload(List<ResourceLocation> effectsIcons) implements CustomPacketPayload {
    public static final Type<SyncBrewManagerIconsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_manager_icons"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewManagerIconsPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeCollection(payload.effectsIcons, FriendlyByteBuf::writeResourceLocation);
                    },
                    buf -> new SyncBrewManagerIconsPayload(buf.readCollection(ArrayList::new, FriendlyByteBuf::readResourceLocation))
            );
}
