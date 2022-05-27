package com.rubby.hyscalper.commands;

import net.minecraft.command.ICommandSender;

import java.io.IOException;

public interface BaseSubCommand {
    String getCommandName();
    String getCommandUsage(ICommandSender sender);

    boolean processCommand(ICommandSender sender, String[] args) throws IOException;
}