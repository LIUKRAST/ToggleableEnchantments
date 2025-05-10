package net.liukrast.toggleable_enchantments;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class NeoForgeEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        TEConstants.onClientTick();
    }
}
