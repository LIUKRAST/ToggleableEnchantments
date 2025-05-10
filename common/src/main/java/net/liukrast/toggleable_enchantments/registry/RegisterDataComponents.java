package net.liukrast.toggleable_enchantments.registry;

import net.liukrast.toggleable_enchantments.TEConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class RegisterDataComponents {
    public static final DataComponentType<ItemEnchantments> DISABLED_ENCHANTMENTS = DataComponentType.<ItemEnchantments>builder()
            .persistent(ItemEnchantments.CODEC)
            .networkSynchronized(ItemEnchantments.STREAM_CODEC)
            .cacheEncoding()
            .build();

    public static final DataComponentType<ItemEnchantments> ENCHANTMENT_GROUPS = DataComponentType.<ItemEnchantments>builder()
            .persistent(ItemEnchantments.CODEC)
            .networkSynchronized(ItemEnchantments.STREAM_CODEC)
            .cacheEncoding()
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TEConstants.id("disabled_enchantments"), DISABLED_ENCHANTMENTS);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TEConstants.id("enchantment_groups"), ENCHANTMENT_GROUPS);
    }
}
