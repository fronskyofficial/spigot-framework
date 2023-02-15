package org.example.fronskyframework.logic.commands;

import org.jetbrains.annotations.NotNull;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.fronskyframework.logic.enums.ELanguage;
import org.example.fronskyframework.logic.interfaces.ICommandHandler;
import org.example.fronskyframework.logic.logging.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandHandler extends Command implements ICommandHandler {

    private final String commandPermission;
    private Player player = null;
    @Setter
    private List<String> subcommands;

    protected CommandHandler(String commandName, String commandPermission) {
        super(commandName);
        setPermission(commandPermission);

        this.commandPermission = commandPermission;
        subcommands = new LinkedList<>();
    }

    public void addSubcommand(String subcommand) {
        subcommands.add(subcommand);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (!subcommands.isEmpty()) {
            String subcommand = getSubcommand(args);
            if (!subcommand.isEmpty() && hasPermission(player, commandPermission + "." + subcommand)) {
                try {
                    Method method = this.getClass().getMethod(subcommand, CommandSender.class, String.class, String[].class);
                    method.invoke(this, sender, commandLabel, getSubcommandArgs(args));
                    return true;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    Logger.logWarning(e.getMessage());
                }
            }
        }

        if (!hasPermission(player, commandPermission)) {
            return false;
        }
        onCommand(sender, commandLabel, args);
        return true;
    }

    /**
     * Check if the player has the specified permission.
     *
     * @param player the player to check for permissions.
     * @param permission the permission to check for.
     * @return boolean false if the player doesn't have the given permission otherwise true.
     */
    protected boolean hasPermission(Player player, String permission) {
        if (player == null) {
            return true;
        }
        if (permission.isEmpty()) {
            Logger.logError("Permissions haven't been set. Make sure to initialize them correctly.");
            return false;
        }

        if (! player.hasPermission(permission)) {
            player.sendMessage(ELanguage.NOPERMISSION.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Get the subcommand from the given arguments.
     *
     * @param args the command arguments.
     * @return String containing the result subCommand, if the subCommand can't be found then an empty string.
     */
    private String getSubcommand(String[] args) {
        if (args.length == 0 || subcommands.isEmpty()) {
            return "";
        }

        for (String subcommand : subcommands) {
            if (subcommand.equalsIgnoreCase(args[0])) {
                return subcommand;
            }
        }
        return "";
    }

    /**
     * Get the arguments for the subcommand.
     *
     * @param args the command arguments.
     * @return String[] containing an array of all args for the subCommand, empty array if it has no args.
     */
    private String[] getSubcommandArgs(String[] args) {
        if (args.length == 0 || subcommands.isEmpty()) {
            return new String[0];
        }

        String[] subcommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subcommandArgs, 0, subcommandArgs.length);
        return subcommandArgs;
    }
}
