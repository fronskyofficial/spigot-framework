package org.example.fronskyframework.logic.modules;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.fronskyframework.logic.commands.CommandHandler;
import org.example.fronskyframework.logic.enums.EModuleStatus;
import org.example.fronskyframework.logic.interfaces.IModule;
import org.example.fronskyframework.logic.logging.Logger;
import org.example.fronskyframework.logic.results.Result;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public abstract class Module<M extends JavaPlugin> implements IModule {

    private final M mainClass;
    @Getter
    private final String moduleName;
    private final List<Listener> events;
    private final List<CommandHandler> commands;
    private final CommandMap commandMap;
    @Getter
    private EModuleStatus moduleStatus = EModuleStatus.IDLE;

    protected Module(M mainClass) {
        this.mainClass = mainClass;
        moduleName = this.getClass().getSimpleName();

        CommandMap tempCommandMap = null;
        try {
            tempCommandMap = (CommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            Logger.logError(e.getMessage());
        }

        if (tempCommandMap == null) {
            Bukkit.shutdown();
        }

        commandMap = tempCommandMap;
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
    protected void event(@NotNull Supplier<? extends Listener> supplier) {
        if (!moduleStatus.equals(EModuleStatus.ENABLED)) {
            throw new RuntimeException("The " + moduleName + " is not enabled.");
        }

        Listener listener = supplier.get();
        Bukkit.getServer().getPluginManager().registerEvents(listener, mainClass);
        events.add(listener);
    }

    /**
     * Registers a command with the module.
     *
     * @param supplier a supplier that provides the command handler.
     * @throws RuntimeException if the module is not enabled.
     */
    protected void command(@NotNull Supplier<? extends CommandHandler> supplier) {
        if (!moduleStatus.equals(EModuleStatus.ENABLED)) {
            throw new RuntimeException("The " + moduleName + " is not enabled.");
        }

        CommandHandler commandHandler = supplier.get();
        commandMap.register(commandHandler.getName(), commandHandler);
        commands.add(commandHandler);
    }
}
