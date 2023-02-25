package org.example.fronskyspigotframework.logic.modules;

import org.bukkit.plugin.java.JavaPlugin;
import org.example.fronskyspigotframework.logic.enums.EModuleStatus;
import org.example.fronskyspigotframework.logic.interfaces.IModule;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ModuleLoader<M extends JavaPlugin> {

    private final Map<Class<? extends IModule>, Module<M>> modules = new HashMap<>();
    private EModuleStatus moduleStatus = EModuleStatus.IDLE;

    /**
     * Prepares the given module for use.
     *
     * @param module The module to prepare.
     * @throws NullPointerException if the module is null.
     */
    public void prepare(@NotNull Module<M> module) {
        if (!modules.containsKey(module.getClass())) {
            modules.put(module.getClass(), module);
        }
    }

    /**
     * Loads all modules.
     *
     * @throws RuntimeException if the loader is not in an idle status.
     */
    public void load() {
        if (!moduleStatus.equals(EModuleStatus.IDLE)) {
            throw new RuntimeException("The modules can't be loaded because the " + this.getClass().getSimpleName() + " is not in an idle status.");
        }

        moduleStatus = EModuleStatus.LOADED;
        modules.values().forEach(Module::load);
    }

    /**
     * Enables all modules.
     *
     * @throws RuntimeException if the loader is not in a loaded status.
     */
    public void enable() {
        if (!moduleStatus.equals(EModuleStatus.LOADED)) {
            throw new RuntimeException("The modules can't be enabled because the " + this.getClass().getSimpleName() + " is not in a loaded status.");
        }

        moduleStatus = EModuleStatus.ENABLED;
        for (Module<M> module : modules.values()) {
            if (! module.enable().Success()) {
                return;
            }
        }
    }

    /**
     * Disables all modules.
     *
     * @throws RuntimeException if the loader is not in an enabled status.
     */
    public void disable() {
        if (!moduleStatus.equals(EModuleStatus.ENABLED)) {
            throw new RuntimeException("The modules can't be disabled because the " + this.getClass().getSimpleName() + " is not in an enabled status.");
        }

        moduleStatus = EModuleStatus.DISABLED;
        modules.values().forEach(Module::disable);
    }
}
