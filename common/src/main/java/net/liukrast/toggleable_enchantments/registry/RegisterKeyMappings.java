package net.liukrast.toggleable_enchantments.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegisterKeyMappings {
    public static final KeyMapping TOGGLEABLE_MENU = new KeyMapping(
            "key.toggleable_enchantments.open_gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "key.categories.toggleable_enchantments"
    );

    public static final List<KeyMapping> GROUP_KEYS = IntStream.range(1, 10).mapToObj(i -> new KeyMapping(
            "key.toggleable_enchantments.group_" + i,
            InputConstants.Type.KEYSYM,
            320 + i,
            "key.categories.toggleable_enchantments"
    )).collect(Collectors.toCollection(ArrayList::new));

    public static void register(Set<KeyMapping> keyMappings) {
        keyMappings.add(TOGGLEABLE_MENU);
        keyMappings.addAll(GROUP_KEYS);
    }

}
