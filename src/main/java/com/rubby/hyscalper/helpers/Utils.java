package com.rubby.hyscalper.helpers;

import com.rubby.hyscalper.core.Processor;
import com.rubby.hyscalper.interfaces.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static void SendMessage(String message)
    {
        String msg = String.format("%s[%s%s-%s] %sHy%s: %s%s",
                EnumChatFormatting.WHITE, EnumChatFormatting.AQUA, EnumChatFormatting.OBFUSCATED,
                EnumChatFormatting.WHITE, EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, EnumChatFormatting.AQUA,
                message);

        IChatComponent comp = new ChatComponentText(msg);
        Minecraft.getMinecraft().thePlayer.addChatMessage(comp);
    }

    public static void SendMessage(String message, String command)
    {
        String msg = String.format("%s[%s%s-%s] %sHy%s: %s%s",
                EnumChatFormatting.WHITE, EnumChatFormatting.AQUA, EnumChatFormatting.OBFUSCATED,
                EnumChatFormatting.WHITE, EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, EnumChatFormatting.AQUA,
                message);

        IChatComponent comp = new ChatComponentText(msg);
        comp.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        Minecraft.getMinecraft().thePlayer.addChatMessage(comp);
    }

    public static File GetConfigFile()
    {
        return new File(Constants.CONFIG_FILE);
    }

    public static Configuration GetConfig() throws IOException {
        String cfgData = FileUtils.readFileToString(GetConfigFile());
        return Processor.Parser.fromJson(cfgData, Configuration.class);
    }

    private static char[] chars = new char[] {'k', 'm', 'b', 't'};

    public static String InMoney(double n, int i) {
        if (n < 1000)
            return String.valueOf(n);

        double d = ((long) n / 100) / 10.0;
        boolean round = (d * 10) %10 == 0;

        return d < 1000 ? (d > 99.9 || round || d > 9.99 ?
                (int) d * 10 / 10 : d + "") + "" + chars[i]
                : InMoney(d, i + 1);
    }

    private static final String NamePattern = "[^A-z0-9 -]";
    private static final String PetPattern = "\\[Lvl [0-9]*] ";
    public static String PureName(String source, Configuration config)
    {
        String name = source;

        if (config.IGNORE_STARS)
            name = name.replaceAll(NamePattern, "");

        if (config.IGNORE_PET_LEVELS)
            name = name.replaceAll(PetPattern, "");

        return name.trim();
    }
}
