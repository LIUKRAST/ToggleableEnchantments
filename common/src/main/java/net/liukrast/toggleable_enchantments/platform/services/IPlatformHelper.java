package net.liukrast.toggleable_enchantments.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface IPlatformHelper {

    boolean isClient();

    void send2S(CustomPacketPayload packet);
}
