package net.liukrast.toggleable_enchantments;

import net.liukrast.toggleable_enchantments.registry.RegisterDataComponents;
import net.liukrast.toggleable_enchantments.registry.RegisterKeyMappings;
import net.liukrast.toggleable_enchantments.registry.RegisterNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashSet;
import java.util.Set;

@Mod(TEConstants.MOD_ID)
public class ToggleableEnchantments {

    public ToggleableEnchantments(IEventBus eventBus) {
        eventBus.register(this);
        eventBus.register(RegisterNetworking.class);
        NeoForge.EVENT_BUS.register(NeoForgeEvents.class);
    }

    @SubscribeEvent
    public void registerKeyMappings(RegisterKeyMappingsEvent event) {
        Set<KeyMapping> keyMappings = new HashSet<>();
        RegisterKeyMappings.register(keyMappings);
        keyMappings.forEach(event::register);
    }

    @SubscribeEvent
    public void registryEvent(RegisterEvent event) {
        event.register(BuiltInRegistries.DATA_COMPONENT_TYPE.key(), helper -> RegisterDataComponents.register());
    }
}
