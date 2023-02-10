package org.example.fronskyframework;

import org.bukkit.plugin.java.JavaPlugin;
import org.example.fronskyframework.logic.modules.ModuleLoader;

public class Main extends JavaPlugin {

    private final ModuleLoader<Main> moduleLoader = new ModuleLoader<>();

    @Override
    public void onLoad() {
        moduleLoader.load();
        System.out.println("Plugin loaded.");
    }

    @Override
    public void onEnable() {
        moduleLoader.enable();
        System.out.println("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        moduleLoader.disable();
        System.out.println("Plugin disabled.");
    }
}