package org.example.logic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.logic.interfaces.ICommandHandler;
import org.example.logic.logging.Logger;
import org.example.logic.results.Result;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandHandler extends Command implements ICommandHandler {

    private final String commandPermission;
    private Player player = null;
    private List<String> subcommands = new LinkedList<>();

    public CommandHandler(String commandName, String commandPermission) {
        super(commandName);
        setPermission(commandPermission);

        this.commandPermission = commandPermission;
    }

    public void setSubcommands(List<String> subcommands) {
        this.subcommands = subcommands;
    }
    public void addSubcommand(String subcommand) {
        subcommands.add(subcommand);
    }
    public void clearSubcommands() {
        subcommands.clear();
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String commandLabel, @Nonnull String[] args) {
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (! subcommands.isEmpty()) {
            String subcommand = getSubcommand(args).Value();
            if (subcommand != null) {
                if (hasPermission(player, commandPermission + "." + subcommand).Success()) {
                    try {
                        Method method = this.getClass().getMethod(subcommand, CommandSender.class, String.class, String[].class);
                        method.invoke(this, sender, commandLabel, getSubcommandArgs(args).Value());
                        return true;
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        Logger.logWarning(e.getMessage());
                    }
                }
            }
        }

        if (! hasPermission(player, commandPermission).Success()) {
            return true;
        }
        onCommand(sender, commandLabel, args);
        return true;
    }

    /**
     * Check if the player has the specified permission.
     *
     * @param player the player to check for permissions.
     * @param permission the permission to check for.
     * @return Result containing the result, it will be OK("Player has the required permissions.") if the player has the required permission,
     *         otherwise it will contain a Fail with an exception message.
     */
    protected Result<String> hasPermission(Player player, String permission) {
        if (player == null) {
            return Result.Ok("Player object is missing, in this case the command should be executed by the console. If not, take appropriate action.");
        }
        if (permission.isEmpty()) {
            Logger.logError("Permissions haven't been set. Make sure to initialize them correctly.");
            return Result.Fail(new Exception("Permissions haven't been set. Make sure to initialize them correctly."));
        }

        if (! player.hasPermission(permission)) {
            player.sendMessage(ChatColor.RED + "You do not have the necessary permissions to perform this action. " +
                                "please contact your system administrator for assistance.");
            return Result.Fail(new Exception("Player doesn't have the necessary permissions to perform this action."));
        }
        return Result.Ok("Player has the required permissions.");
    }

    /**
     * Get the subcommand from the given arguments.
     *
     * @param args the command arguments.
     * @return Result containing the result, it will be OK(subcommand) if a subcommand is found,
     *         otherwise it will contain a Fail with an exception message.
     */
    private Result<String> getSubcommand(String[] args) {
        if (args.length == 0 || subcommands.isEmpty()) {
            return Result.Fail(new Exception("Either the number of arguments or the list of subcommands is missing."));
        }

        for (String subcommand : subcommands) {
            if (subcommand.equalsIgnoreCase(args[0])) {
                return Result.Ok(subcommand);
            }
        }
        return Result.Fail(new Exception("Subcommand was not found."));
    }

    /**
     * Get the arguments for the subcommand.
     *
     * @param args the command arguments.
     * @return Result containing the result, it will be OK(subcommandArgs) if a subcommand is found,
     *         otherwise it will contain a Fail with an exception message.
     */
    private Result<String[]> getSubcommandArgs(String[] args) {
        if (args.length == 0 || subcommands.isEmpty()) {
            return Result.Fail(new Exception("Either the number of arguments or the list of subcommands is missing."));
        }

        String[] subcommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subcommandArgs, 0, subcommandArgs.length);
        return Result.Ok(subcommandArgs);
    }
}
