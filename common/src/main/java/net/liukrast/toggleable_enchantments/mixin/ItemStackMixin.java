package net.liukrast.toggleable_enchantments.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.liukrast.toggleable_enchantments.mixin_helpers.IFlagEnchantment;
import net.liukrast.toggleable_enchantments.registry.RegisterDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", ordinal = 3, shift = At.Shift.AFTER))
    private void toggleable_enchantments$getTooltipLines(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local Consumer<Component> consumer) {
        var that = ItemStack.class.cast(this);
        ItemEnchantments disabled = that.get(RegisterDataComponents.DISABLED_ENCHANTMENTS);
        if(disabled == null) return;
        ((IFlagEnchantment)disabled).toggleable_enchantments$setDisabled(true);
        disabled.addToTooltip(tooltipContext, consumer, tooltipFlag);
        ((IFlagEnchantment)disabled).toggleable_enchantments$setDisabled(false);
    }
}
