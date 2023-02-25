package org.example.fronskyspigotframework;

import org.bukkit.plugin.java.JavaPlugin;
import org.example.fronskyspigotframework.logic.logging.Logger;
import org.example.fronskyspigotframework.logic.modules.ModuleLoader;

public class Main extends JavaPlugin {

    private final ModuleLoader<Main> moduleLoader = new ModuleLoader<>();

    @Override
    public void onLoad() {
        moduleLoader.load();
        Logger.logInfo("Plugin loaded.");
    }

    @Override
    public void onEnable() {
        moduleLoader.enable();
        Logger.logInfo("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        moduleLoader.disable();
        Logger.logInfo("Plugin disabled.");
    }
}