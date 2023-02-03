package org.example.logic.modules;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.logic.commands.CommandHandler;
import org.example.logic.enums.EModuleStatus;
import org.example.logic.events.EventHandler;
import org.example.logic.interfaces.IModule;
import org.example.logic.logging.Logger;
import org.example.logic.results.Result;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public abstract class Module<M extends JavaPlugin> implements IModule {

    private final M mainClass;
    @Getter
    private final String moduleName;
    private final List<EventHandler> events;
    private final List<CommandHandler> commands;
    private final CommandMap commandMap;
    @Getter
    private EModuleStatus moduleStatus = EModuleStatus.IDLE;

    public Module(M mainClass) {
        this.mainClass = mainClass;
        moduleName = this.getClass().getSimpleName();

        CommandMap _commandMap = null;
        try {
            _commandMap = (CommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            Logger.logError(e.getMessage());
        }

        if (_commandMap == null) {
            Bukkit.shutdown();
        }

        commandMap = _commandMap;
        events = new LinkedList<>();
        commands = new LinkedList<>();
    }

    /**
     * Loads the module.
     *
     * @throws RuntimeException if an attempt is made to load the module while it is already loaded.
     */
    public void load() {
        if (!moduleStatus.equals(EModuleStatus.IDLE)) {
            throw new RuntimeException("An attempt was made to load the " + moduleName + " while it was already loaded.");
        }

        Logger.logInfo("Loading " + moduleName + "...");
        moduleStatus = EModuleStatus.LOADED;
        onLoad();
    }

    /**
     * Enables the module.
     *
     * @return Result containing the result, it will be OK("Module has been successfully enabled.") if the module is enabled successfully,
     *         otherwise it will contain a Fail with an exception message.
     * @throws RuntimeException if the module is not in a loaded status.
     */
    public Result<String> enable() {
        if (!moduleStatus.equals(EModuleStatus.LOADED)) {
            throw new RuntimeException("An attempt was made to enable the " + moduleName + " while it was not loaded.");
        }

        Logger.logInfo("Enabling " + moduleName + "...");
        moduleStatus = EModuleStatus.ENABLED;
        try {
            onEnable();
            return Result.Ok("Module has been successfully enabled.");
        } catch (Exception e) {
            moduleStatus = EModuleStatus.DISABLED;
            Logger.logError(e.getMessage());
            Bukkit.shutdown();
            return Result.Fail(e);
        }
    }

    /**
     * Disables the module.
     *
     * @throws RuntimeException if an attempt is made to disable the module while it is not enabled.
     */
    public void disable() {
        if (!moduleStatus.equals(EModuleStatus.ENABLED)) {
            throw new RuntimeException("An attempt was made to disable the " + moduleName + " while it was not enabled.");
        }

        int amountOfComponents = events.size() + commands.size();
        Logger.logInfo("Disabling " + moduleName + ", removing " + amountOfComponents + " components...");
        moduleStatus = EModuleStatus.DISABLED;

        events.forEach(HandlerList::unregisterAll);
        for (CommandHandler commandHandler : commands) {
            commandHandler.unregister(commandMap);
        }

        events.clear();
        commands.clear();
        onDisable();
    }

    /**
     * Registers an event with the module.
     *
     * @param supplier a supplier that provides the event handler.
     * @throws RuntimeException if the module is not enabled.
     */
    protected void event(@Nonnull Supplier<? extends EventHandler> supplier) {
        if (!moduleStatus.equals(EModuleStatus.ENABLED)) {
            throw new RuntimeException("The " + moduleName + " is not enabled.");
        }

        EventHandler eventHandler = supplier.get();
        Bukkit.getServer().getPluginManager().registerEvents(eventHandler, mainClass);
        events.add(eventHandler);
    }

    /**
     * Registers a command with the module.
     *
     * @param supplier a supplier that provides the command handler.
     * @throws RuntimeException if the module is not enabled.
     */
    protected void command(@Nonnull Supplier<? extends CommandHandler> supplier) {
        if (!moduleStatus.equals(EModuleStatus.ENABLED)) {
            throw new RuntimeException("The " + moduleName + " is not enabled.");
        }

        CommandHandler commandHandler = supplier.get();
        commandMap.register(commandHandler.getName(), commandHandler);
        commands.add(commandHandler);
    }
}
