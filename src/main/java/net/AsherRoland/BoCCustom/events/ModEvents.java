package net.AsherRoland.BoCCustom.events;

import net.AsherRoland.BoCCustom.BocCustom;
import net.AsherRoland.BoCCustom.commands.recyclingReset;
import net.createmod.catnip.command.ConfigCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BocCustom.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new recyclingReset(event.getDispatcher());

        ConfigCommand.register();
    }
}
