package net.liukrast.toggleable_enchantments.platform;

import net.liukrast.toggleable_enchantments.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public boolean isClient() {
        return FMLEnvironment.getDist().isClient();
    }

    @Override
    public void send2S(CustomPacketPayload packet) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ServerboundCustomPayloadPacket(packet));
        }
    }
}
