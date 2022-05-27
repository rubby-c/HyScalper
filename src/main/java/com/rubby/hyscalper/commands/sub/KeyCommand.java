package com.rubby.hyscalper.commands.sub;

import com.rubby.hyscalper.commands.BaseSubCommand;
import com.rubby.hyscalper.core.Processor;
import com.rubby.hyscalper.helpers.Utils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class KeyCommand implements BaseSubCommand {
    public KeyCommand() { }

    @Override
    public String getCommandName() {
        return "key";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return String.format("%s<string> %s- %sSet you api key(s). Use %s/hy key add [key] %sif you wanna use more than one.",
                EnumChatFormatting.WHITE, EnumChatFormatting.YELLOW, EnumChatFormatting.AQUA,
                EnumChatFormatting.WHITE, EnumChatFormatting.AQUA);
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] args) {
        String msg;
        switch (args.length)
        {
            case 1:
            {
                Processor.Configuration.API_KEYS.clear();
                msg = String.format("Api Key set to %s%s", EnumChatFormatting.WHITE, args[0]);
                Processor.Configuration.API_KEYS.add(args[0]);
                break;
            }
            case 2:
            {
                msg = String.format("Api Key added to list + %s%s", EnumChatFormatting.WHITE, args[1]);
                Processor.Configuration.API_KEYS.add(args[1]);
                break;
            }

            default:
                msg = EnumChatFormatting.RED + "Error! Command usage: /hy key [apikey]";
                break;
        }


        try {
            FileUtils.writeStringToFile(Utils.GetConfigFile(), Processor.Parser.toJson(Processor.Configuration));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Utils.SendMessage(msg);

        return true;
    }
}
