package net.liukrast.toggleable_enchantments.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.liukrast.toggleable_enchantments.registry.RegisterDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private static void toggleable_enchantments$updateEnchantments(ItemStack stack, Consumer<ItemEnchantments.Mutable> updater, CallbackInfoReturnable<ItemEnchantments> cir, @Local ItemEnchantments.Mutable mutable) {
        ItemEnchantments disabled = stack.getOrDefault(RegisterDataComponents.DISABLED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        disabled.entrySet().forEach(e->mutable.set(e.getKey(), e.getIntValue()));
        stack.set(RegisterDataComponents.DISABLED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        stack.set(RegisterDataComponents.ENCHANTMENT_GROUPS, ItemEnchantments.EMPTY);
    }

    @Inject(method = "setEnchantments", at = @At("HEAD"))
    private static void toggleable_enchantments$setEnchantments(ItemStack stack, ItemEnchantments enchantments, CallbackInfo ci) {
        ItemEnchantments disabled = stack.getOrDefault(RegisterDataComponents.DISABLED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        var mutable = new ItemEnchantments.Mutable(disabled);
        mutable.removeIf(holder -> enchantments.entrySet().stream().anyMatch(holder1 -> holder.getRegisteredName().equals(holder1.getKey().getRegisteredName())));
        stack.set(RegisterDataComponents.DISABLED_ENCHANTMENTS, mutable.toImmutable());
        stack.set(RegisterDataComponents.ENCHANTMENT_GROUPS, ItemEnchantments.EMPTY);
    }

    @Inject(method = "getEnchantmentsForCrafting", at = @At("RETURN"), cancellable = true)
    private static void toggleable_enchantments$getEnchantmentsForCrafting(ItemStack stack, CallbackInfoReturnable<ItemEnchantments> cir) {
        ItemEnchantments disabled = stack.getOrDefault(RegisterDataComponents.DISABLED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments enabled = cir.getReturnValue();
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enabled);
        disabled.entrySet().forEach(e -> mutable.upgrade(e.getKey(), e.getIntValue()));
        cir.setReturnValue(mutable.toImmutable());
    }

    @Inject(method = "hasAnyEnchantments", at = @At("RETURN"), cancellable = true)
    private static void toggleable_enchantments$hasAnyEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() || !stack.getOrDefault(RegisterDataComponents.DISABLED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty());
    }
}
