package com.rubby.hyscalper.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ModEvents {
    @SubscribeEvent
    public void onEntityJoinWorld(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        //TODO: Make this work.
    }
}
