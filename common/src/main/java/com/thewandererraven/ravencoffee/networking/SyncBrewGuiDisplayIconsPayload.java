package com.thewandererraven.ravencoffee.networking;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SyncBrewGuiDisplayIconsPayload(List<ResourceLocation> effectIcons) implements CustomPacketPayload {
    public static final Type<SyncBrewGuiDisplayIconsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_brew_gui_display_icons_duration"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncBrewGuiDisplayIconsPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeCollection(payload.effectIcons(), ResourceLocation.STREAM_CODEC);
                    },
                    buf -> new SyncBrewGuiDisplayIconsPayload(buf.readList(ResourceLocation.STREAM_CODEC))
            );

}
