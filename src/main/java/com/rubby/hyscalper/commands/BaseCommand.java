package com.rubby.hyscalper.commands;

import com.google.common.base.Objects;
import com.rubby.hyscalper.helpers.Constants;
import com.rubby.hyscalper.helpers.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BaseCommand extends CommandBase {
    private final BaseSubCommand[] Subcommands;

    public BaseCommand(BaseSubCommand[] subcommands) {
        this.Subcommands = subcommands;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "hy";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/hy <subcommand> <arguments>";
    }

    private void Help(ICommandSender sender) {
        List<String> commandUsages = new LinkedList<>();

        for (BaseSubCommand subcommand : this.Subcommands) {
            commandUsages.add(EnumChatFormatting.AQUA + "/hy " + subcommand.getCommandName() + " " + subcommand.getCommandUsage(sender));
        }

        for (int i = 0; i < commandUsages.size(); i++)
        {
            ChatComponentText message;
            if (i == 0) {
                message = new ChatComponentText(
                        EnumChatFormatting.AQUA + "Hy " +
                                EnumChatFormatting.GREEN + "v" +
                                Constants.VERSION + "\n" + commandUsages.get(i));
            }
            else {
                message = new ChatComponentText(commandUsages.get(i));
            }

            sender.addChatMessage(message);
        }
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Help(sender);
            return;
        }

        for (BaseSubCommand subcommand : this.Subcommands) {
            if (Objects.equal(args[0], subcommand.getCommandName())) {
                try {
                    if (!subcommand.processCommand(sender, Arrays.copyOfRange(args, 1, args.length))) {
                        Utils.SendMessage("Command failed, use: /hy " + subcommand.getCommandName() + " " + subcommand.getCommandUsage(sender));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
            }
        }
        Utils.SendMessage("§cThe subcommand wasn't found, please refer to the help message below for the list of subcommands.");
    }
}