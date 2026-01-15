package net.AsherRoland.BoCCustom;

import com.mojang.logging.LogUtils;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlockRenderer;
import net.AsherRoland.BoCCustom.indexing.SoPAllBlockEntityTypes;
import net.AsherRoland.BoCCustom.indexing.SoPAllBlocks;
import net.AsherRoland.BoCCustom.indexing.SoPAllItems;
import net.AsherRoland.BoCCustom.indexing.SoPCreativeModeTabs;
import net.AsherRoland.BoCCustom.network.ModNetworking;
import net.AsherRoland.BoCCustom.rendering.AllPartialModels;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        }
    }
}
