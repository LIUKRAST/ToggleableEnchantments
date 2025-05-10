package net.liukrast.toggleable_enchantments.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.liukrast.toggleable_enchantments.platform.services.IPlatformHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    }

    @Override
    public void send2S(CustomPacketPayload packet) {
        ClientPlayNetworking.send(packet);
    }
}
