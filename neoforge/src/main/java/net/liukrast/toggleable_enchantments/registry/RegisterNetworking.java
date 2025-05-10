package net.liukrast.toggleable_enchantments.registry;

import net.liukrast.toggleable_enchantments.TEConstants;
import net.liukrast.toggleable_enchantments.packet.ChangeGroupPacket;
import net.liukrast.toggleable_enchantments.packet.ToggleEnchantmentPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class RegisterNetworking {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final var registry = event.registrar(TEConstants.MOD_ID).versioned(TEConstants.PACKET_VERSION);
        registry.playToServer(ToggleEnchantmentPacket.PACKET_TYPE, ToggleEnchantmentPacket.CODEC, (p, c) -> ToggleEnchantmentPacket.handle(p, c.player()));
        registry.playToServer(ChangeGroupPacket.PACKET_TYPE, ChangeGroupPacket.CODEC, (p, c) -> ChangeGroupPacket.handle(p, c.player()));
    }
}
