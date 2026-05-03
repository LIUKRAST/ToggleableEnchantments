package net.liukrast.toggleable_enchantments.platform;

import net.liukrast.toggleable_enchantments.platform.services.IPlatformHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }

    @Override
    public void send2S(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }
}
