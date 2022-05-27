package com.rubby.hyscalper.commands.sub;

import com.rubby.hyscalper.commands.BaseSubCommand;
import com.rubby.hyscalper.helpers.Utils;
import net.minecraft.command.ICommandSender;

import java.awt.*;
import java.io.IOException;

public class ConfigurateCommand implements BaseSubCommand {
    public ConfigurateCommand() { }

    @Override
    public String getCommandName() {
        return "cfg";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "<none> - Opens your config file for further editing.";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] args) {
        Desktop desktop = Desktop.getDesktop();
        if (Utils.GetConfigFile().isFile())
        {
            try {
                desktop.open(Utils.GetConfigFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
