package org.example.fronskyspigotframework.logic.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.example.fronskyspigotframework.logic.enums.ELanguage;
import org.example.fronskyspigotframework.logic.interfaces.ICommandHandler;
import org.example.fronskyspigotframework.logic.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandHandler implements TabCompleter, CommandExecutor, ICommandHandler {

    @Getter
    private final String name;
    @Getter
    private final String permission;
    private Player player = null;
    @Setter
    private List<String> subcommands;

    protected CommandHandler(String name, String permission) {
        this.name = name;
        this.permission = permission;
        subcommands = new LinkedList<>();
    }

    public void addSubcommand(String subcommand) {
        subcommands.add(subcommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (!subcommands.isEmpty()) {
            String subcommand = getSubcommand(args);
            if (!subcommand.isEmpty() && hasPermission(player, permission + "." + subcommand)) {
                try {
                    Method method = this.getClass().getMethod(subcommand, CommandSender.class, String.class, String[].class);
                    method.invoke(this, sender, label, getSubcommandArgs(args));
                    return true;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    Logger.logWarning(e.getMessage());
                }
            }
        }

        if (!hasPermission(player, permission)) {
            return false;
        }
        onCommand(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new LinkedList<>();

        if (args.length == 1) {
            subcommands.stream()
                    .filter(subcommand -> subcommand.startsWith(args[0]) && hasPermission(player, permission + "." + subcommand))
                    .forEach(completions::add);
        } else {
            String subcommand = getSubcommand(args);
            if (!subcommand.isEmpty() && hasPermission(player, permission + "." + subcommand)) {
                try {
                    Method method = this.getClass().getMethod(subcommand + "TabComplete", CommandSender.class, String.class, String[].class);
                    List<String> tabCompletions = (List<String>) method.invoke(this, sender, alias, args);
                    completions.addAll(tabCompletions);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    Logger.logWarning(e.getMessage());
                }
            }
        }

        return completions;
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
