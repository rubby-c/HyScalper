package com.rubby.hyscalper.commands.sub;

import com.rubby.hyscalper.commands.BaseSubCommand;
import com.rubby.hyscalper.core.Processor;
import com.rubby.hyscalper.helpers.Utils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class ToggleCommand implements BaseSubCommand {
    public ToggleCommand() { }

    @Override
    public String getCommandName() {
        return "toggle";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "<none> - Toggles the scalper.";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] args) {
        if (Processor.Configuration.API_KEYS.size() <= 0)
        {
            String msg = String.format("%sPlease enter your api key first using %s/hy key [apikey]",
                    EnumChatFormatting.AQUA, EnumChatFormatting.WHITE);

            Utils.SendMessage(msg);
            return true;
        }

        Processor.ToggleBoolean = !Processor.ToggleBoolean;

        Utils.SendMessage("Scalper alerts " + (Processor.ToggleBoolean ? "enabled!" : "disabled!"));

        return true;
    }
}
