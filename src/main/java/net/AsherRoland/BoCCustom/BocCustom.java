package net.AsherRoland.BoCCustom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlockRenderer;
import net.AsherRoland.BoCCustom.commands.recyclingReset;
import net.AsherRoland.BoCCustom.indexing.*;
import net.AsherRoland.BoCCustom.network.ModNetworking;
import net.AsherRoland.BoCCustom.ponder.BocPonderPlugin;
import net.AsherRoland.BoCCustom.rendering.AllPartialModels;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BocCustom.MODID)
public class BocCustom {
    public static final String MODID = "boc_custom";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BocCustom(FMLJavaModLoadingContext context) {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        SoPAllBlocks.REGISTRATE.registerEventListeners(modBus);

        SoPAllBlocks.register();
        SoPAllBlockEntityTypes.register();
        AllPartialModels.init();
        SoPCreativeModeTabs.register(modBus);

        LOGGER.info("BocCustom constructor called");

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BlockEntityRenderers.register(
                    SoPAllBlockEntityTypes.RECYCLING_BLOCK.get(),
                    RecyclingBlockRenderer::new
            );
            ModNetworking.register();
            PonderIndex.addPlugin(new BocPonderPlugin());
        }
    }
}
