package com.rubby.hyscalper;

import com.google.gson.GsonBuilder;
import com.rubby.hyscalper.commands.BaseCommand;
import com.rubby.hyscalper.commands.BaseSubCommand;
import com.rubby.hyscalper.commands.sub.ConfigurateCommand;
import com.rubby.hyscalper.commands.sub.KeyCommand;
import com.rubby.hyscalper.commands.sub.ReloadCfgCommand;
import com.rubby.hyscalper.commands.sub.ToggleCommand;
import com.rubby.hyscalper.core.Processor;
import com.rubby.hyscalper.events.ModEvents;
import com.rubby.hyscalper.helpers.Constants;
import com.rubby.hyscalper.helpers.DiscordWebhook;
import com.rubby.hyscalper.helpers.Utils;
import com.rubby.hyscalper.interfaces.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

@Mod(modid = HyScalper.MODID, version = Constants.VERSION)
public class HyScalper
{
    public static final String MODID = "hyscalper";
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) throws IOException {
        Processor.Parser = new GsonBuilder().setPrettyPrinting().create();
        Processor.Configuration = Utils.GetConfigFile().isFile() ? Utils.GetConfig() : new Configuration();
    }

    public Timer timer = new Timer();

    @EventHandler
    public void init (FMLInitializationEvent event) throws IOException {
        ClientCommandHandler.instance.registerCommand(new BaseCommand(new BaseSubCommand[] {
                new KeyCommand(),
                new ToggleCommand(),
                new ConfigurateCommand(),
                new ReloadCfgCommand()
        }));

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Processor.ToggleBoolean)
                {
                    try {
                        Processor.Process();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, Processor.Configuration.COOLDOWN * 1000, Processor.Configuration.COOLDOWN * 1000);
    }
}
