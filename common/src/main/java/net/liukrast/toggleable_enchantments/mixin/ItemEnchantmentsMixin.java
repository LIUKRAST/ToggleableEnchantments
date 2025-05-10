package net.liukrast.toggleable_enchantments.mixin;

import net.liukrast.toggleable_enchantments.mixin_helpers.IFlagEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEnchantments.class)
@Debug(export = true)
public class ItemEnchantmentsMixin implements IFlagEnchantment {

    @Unique
    boolean toggleable_enchantments$disabled;

    @ModifyArg(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private <T> T toggleable_enchantments$addToTooltip(T t) {
        if(!toggleable_enchantments$disabled) return t;
        Component arg = (Component) t;
        @SuppressWarnings("all")
        T returnValue = (T)Component.literal("❌ ").withStyle(ChatFormatting.RED).append(arg);
        return returnValue;
    }

    @Override
    public boolean toggleable_enchantments$setDisabled(boolean disabled) {
        return this.toggleable_enchantments$disabled = disabled;
    }
}
