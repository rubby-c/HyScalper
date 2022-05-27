package com.rubby.hyscalper.commands.sub;

import com.rubby.hyscalper.commands.BaseSubCommand;
import com.rubby.hyscalper.core.Processor;
import com.rubby.hyscalper.helpers.Utils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;

public class ReloadCfgCommand implements BaseSubCommand {
    public ReloadCfgCommand() { }

    @Override
    public String getCommandName() {
        return "reloadcfg";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "<none> - Reloads the mod to apply config changes.";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] args) throws IOException {
        if (Utils.GetConfigFile().isFile())
        {
            Processor.Configuration = Utils.GetConfig();
            Utils.SendMessage("Mod reloaded! New config is in effect.");
        }
        else
        {
            String msg = String.format("%sPlease enter your api key first using %s/hy key [apikey]",
                    EnumChatFormatting.AQUA, EnumChatFormatting.WHITE);

            Utils.SendMessage(msg);
        }

        return true;
    }
}
