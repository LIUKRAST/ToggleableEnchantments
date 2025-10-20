package net.liukrast.toggleable_enchantments.screen;

import net.liukrast.toggleable_enchantments.TEConstants;
import net.liukrast.toggleable_enchantments.packet.ChangeGroupPacket;
import net.liukrast.toggleable_enchantments.packet.ToggleEnchantmentPacket;
import net.liukrast.toggleable_enchantments.platform.TEServices;
import net.liukrast.toggleable_enchantments.registry.RegisterDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import org.lwjgl.system.NonnullDefault;

import java.util.*;
import java.util.stream.Collectors;

@NonnullDefault
public class TEScreen extends Screen {
    public static final ResourceLocation TEXTURE = TEConstants.id("textures/gui/toggleable_enchantments.png");
    public static final ResourceLocation BUTTON = TEConstants.id("toggle_button");
    public static final Component TITLE = Component.translatable("container.toggleable_enchantments");

    private static final List<Component> TOOLTIP = Arrays.asList(new Component[]{
            Component.translatable("container.toggleable_enchantments.hotkey_group"),
            Component.translatable("container.toggleable_enchantments.hotkey_group.help")
    });

    private static final int IMAGE_W = 176, IMAGE_H = 144;
    private static final int TOP_OFFSET = 19;
    private static final int BUTTON_W = 16, BUTTON_OFFSET = 7;
    public TEScreen() {
        super(TITLE);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int leftPos = (this.width- IMAGE_W)>>1;
        int topPos = (this.height- IMAGE_H)>>1;
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, leftPos, topPos, 0, 0, IMAGE_W, IMAGE_H, 256, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int leftPos = (this.width-IMAGE_W)>>1;
        int topPos = (this.height-IMAGE_H)>>1;
        int i = 0;
        boolean tooltip = false;
        for(var entry : enchantmentsEntrySet()) {
            var subEntry = entry.getKey();
            var holder = subEntry.getKey();
            boolean enabled = entry.getValue();
            int level = subEntry.getIntValue();
            assert Minecraft.getInstance().player != null;
            int group = Minecraft.getInstance().player.getMainHandItem().getOrDefault(RegisterDataComponents.ENCHANTMENT_GROUPS, ItemEnchantments.EMPTY).getLevel(holder);
            if(level > 0) {
                var comp = Enchantment.getFullname(holder, level).plainCopy().withStyle(ChatFormatting.WHITE);
                boolean hovered = mouseX >= leftPos + IMAGE_W - BUTTON_W - BUTTON_OFFSET && mouseX < leftPos + 176 - BUTTON_OFFSET && mouseY >= topPos + i*12 + TOP_OFFSET && mouseY < topPos + i*12 + 8 + TOP_OFFSET;
                guiGraphics.drawString(this.font, comp, leftPos + 8, topPos + i*12 + TOP_OFFSET, -1);
                guiGraphics.drawString(this.font, group == 0 ? "-" : String.valueOf(group), leftPos + 130, topPos + i*12 + TOP_OFFSET, -1);
                if(holder.is(TEConstants.WHITELIST) || (!holder.is(EnchantmentTags.CURSE) && !holder.is(TEConstants.BLACKLIST)))
                    guiGraphics.blitSprite(
                            RenderType::guiTextured,
                            BUTTON,
                            32, 16,
                            (hovered ? BUTTON_W : 0),
                            enabled ? 0 : 8,
                            leftPos + IMAGE_W - BUTTON_W - BUTTON_OFFSET,
                            topPos + i*12 + TOP_OFFSET,
                            BUTTON_W, 8
                    );
                tooltip |= mouseX >= leftPos + 130 && mouseX < leftPos + 152 && mouseY >= topPos + i*12 + TOP_OFFSET -2 && mouseY < topPos + i*12 + TOP_OFFSET + 10;
            }
            i++;
        }
        if(tooltip) guiGraphics.renderTooltip(this.font, TOOLTIP, Optional.empty(), mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int leftPos = (this.width-IMAGE_W)>>1;
        int topPos = (this.height-IMAGE_H)>>1;
        int i = 0;
        for(var entry : enchantmentsEntrySet()) {
            var subEntry = entry.getKey();
            var holder = subEntry.getKey();
            int level = subEntry.getIntValue();
            if(level > 0) {
                boolean hovered = mouseX >= leftPos + IMAGE_W - BUTTON_W - BUTTON_OFFSET && mouseX < leftPos + 176 - BUTTON_OFFSET && mouseY >= topPos + i*12 + TOP_OFFSET && mouseY < topPos + i*12 + 8 + TOP_OFFSET;
                i++;
                if(!holder.is(TEConstants.WHITELIST) && (holder.is(EnchantmentTags.CURSE) || holder.is(TEConstants.BLACKLIST))) continue;
                if(!hovered) continue;
                var level1 = Minecraft.getInstance().level;
                if(level1 == null) continue;
                var access = level1.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                ResourceLocation id = access.getKey(holder.value());
                if(id == null) continue;
                TEServices.PLATFORM.send2S(new ToggleEnchantmentPacket(List.of(id), EquipmentSlot.MAINHAND));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int leftPos = (this.width-IMAGE_W)>>1;
        int topPos = (this.height-IMAGE_H)>>1;
        int i = 0;
        for(var entry : enchantmentsEntrySet()) {
            var subEntry = entry.getKey();
            var holder = subEntry.getKey();
            int level = subEntry.getIntValue();
            assert Minecraft.getInstance().player != null;
            int group = Minecraft.getInstance().player.getMainHandItem().getOrDefault(RegisterDataComponents.ENCHANTMENT_GROUPS, ItemEnchantments.EMPTY).getLevel(holder);
            if(level > 0) {
                boolean hovered = mouseX >= leftPos + 130 && mouseX < leftPos + 152 && mouseY >= topPos + i*12 + TOP_OFFSET -2 && mouseY < topPos + i*12 + TOP_OFFSET + 10;
                i++;
                if(!hovered) continue;
                var level1 = Minecraft.getInstance().level;
                if(level1 == null) continue;
                var access = level1.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                ResourceLocation id = access.getKey(holder.value());
                if(id == null) continue;
                TEServices.PLATFORM.send2S(new ChangeGroupPacket(id, (int) Math.clamp(group + scrollY, 0, 9)));
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private Set<Map.Entry<Entry<Holder<Enchantment>>, Boolean>> enchantmentsEntrySet() {
        var player = Minecraft.getInstance().player;
        if(player == null) {
            this.onClose();
            return new HashSet<>();
        }
        var stack = player.getMainHandItem();
        if(stack.isEmpty()) this.onClose();
        var enabled = stack.getEnchantments();
        var disabled = stack.getOrDefault(RegisterDataComponents.DISABLED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        final Map<Entry<Holder<Enchantment>>, Boolean> map = new HashMap<>();
        enabled.entrySet().forEach(e -> map.put(e, true));
        disabled.entrySet().forEach(e -> map.put(e, false));
        return map.entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getKey().getKey().getRegisteredName()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
