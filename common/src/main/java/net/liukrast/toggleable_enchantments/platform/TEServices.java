package net.liukrast.toggleable_enchantments.platform;

import net.liukrast.toggleable_enchantments.TEConstants;
import net.liukrast.toggleable_enchantments.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

public class TEServices {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        TEConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
