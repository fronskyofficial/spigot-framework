package org.example.fronskyframework.logic.interfaces;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface ICommandHandler {

    /**
     * Executes the command.
     *
     * @param sender Source object which is executing this command.
     * @param commandLabel The alias of the command used.
     * @param args All arguments passed to the command, split via ' '.
     * @throws NullPointerException if sender, commandLabel or args is null.
     */
    public void onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args);
}
