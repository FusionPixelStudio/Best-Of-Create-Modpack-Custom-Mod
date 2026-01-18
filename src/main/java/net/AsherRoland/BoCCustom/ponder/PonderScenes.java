package net.AsherRoland.BoCCustom.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.AsherRoland.BoCCustom.BocLang;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import slimeknights.mantle.block.entity.InventoryBlockEntity;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.fluid.FluidTankAnimated;
import slimeknights.tconstruct.library.fluid.FluidTankBase;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.CastingTableBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock;
import slimeknights.tconstruct.smeltery.block.controller.HeaterBlock;
import slimeknights.tconstruct.smeltery.block.controller.MelterBlock;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.FaucetBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.TankBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.MelterBlockEntity;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;
import static slimeknights.tconstruct.smeltery.TinkerSmeltery.heater;

public class PonderScenes {
    public static void searedMelter(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("seared_melter", BocLang.translateDirect("ponder.seared_smelter.header").getString());

        scene.showBasePlate();

        var smelter_link = scene.world().showIndependentSection(util.select().fromTo(2, 2, 2, 2, 3, 2), Direction.DOWN);
        scene.world().moveSection(smelter_link, util.vector().of(0, -1, 0), 0);

        scene.idle(40);

        scene.overlay().showText(50)
                        .text("This is a basic smelter setup")
                        .attachKeyFrame()
                        .placeNearTarget()
                        .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH));

        scene.idle(50);

        ItemStack coal = new ItemStack(Items.COAL);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.NORTH), Pointing.UP, 40)
                        .rightClick().withItem(coal);
        scene.world().modifyBlock(util.grid().at(2, 2, 2), s -> s.setValue(HeaterBlock.ACTIVE, true), false);
        scene.overlay().showText(40)
                        .text("Use any fuel type")
                        .attachKeyFrame()
                        .placeNearTarget()
                        .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH));

        scene.idle(40);

        SearedTankBlock searedTank = TinkerSmeltery.searedTank.get(SearedTankBlock.TankType.FUEL_TANK);
        ItemStack lava = new ItemStack(Items.LAVA_BUCKET);

        scene.world().setBlock(util.grid().at(2, 2, 2), searedTank.defaultBlockState(), true);
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.NORTH), Pointing.UP, 40)
                .rightClick().withItem(lava);
        scene.world().modifyBlockEntity(util.grid().at(2, 2, 2), TankBlockEntity.class, be -> {
            be.getTank().setFluid(
                    new FluidStack(Fluids.LAVA, 4000)
            );
        });
        scene.overlay().showText(40)
                .text("Or use a fuel tank with lava")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH));
        scene.idle(40);

        scene.world().restoreBlocks(util.select().fromTo(2, 2, 2, 2, 2, 2));
        var left_basin = scene.world().showIndependentSection(util.select().fromTo(1, 2, 2, 1, 3, 2), Direction.DOWN);
        scene.world().moveSection(left_basin, util.vector().of(0, -1, 0), 0);
        var right_basin = scene.world().showIndependentSection(util.select().fromTo(3, 2, 2, 3, 3, 2), Direction.DOWN);
        scene.world().moveSection(right_basin, util.vector().of(0, -1, 0), 0);

        scene.idle(20);

        ItemStack raw_iron = new ItemStack(Items.RAW_IRON);

        scene.world().modifyBlockEntity(util.grid().at(2, 3, 2), MelterBlockEntity.class, be ->{
            be.getItemHandler().insertItem(0, raw_iron, false);
            be.getItemHandler().insertItem(1, raw_iron, false);
            be.getItemHandler().insertItem(2, raw_iron, false);
        });
        scene.world().modifyBlock(util.grid().at(2, 2, 2), s -> s.setValue(HeaterBlock.ACTIVE, true), false);
        scene.world().modifyBlock(util.grid().at(2, 3, 2), s -> s.setValue(MelterBlock.ACTIVE, true), false);
        scene.idle(20);
        scene.overlay().showText(40)
                .text("Add ore to the melter")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH));
        scene.idle(60);
        scene.overlay().showText(40)
                .text("And wait...")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH));
        scene.idle(60);

        FluidStack ironFluid = new FluidStack(TinkerFluids.moltenIron.getStill(), 360);

        scene.world().modifyBlockEntity(util.grid().at(2, 3, 2), MelterBlockEntity.class, be -> {
            be.getTank().setFluid(ironFluid);
        });
        scene.world().modifyBlockEntity(util.grid().at(2, 3, 2), MelterBlockEntity.class, be ->{
            be.getItemHandler().extractItem(0, 1, false);
            be.getItemHandler().extractItem(1, 1, false);
            be.getItemHandler().extractItem(2, 1, false);
        });
        scene.idle(20);

        ItemStack ingot_cast = new ItemStack(TinkerSmeltery.ingotCast.get());

        scene.world().modifyBlockEntity(util.grid().at(1, 2, 2), CastingBlockEntity.Table.class, be ->{
            be.setItem(0, ingot_cast);
        });

        scene.world().modifyBlock(util.grid().at(2, 2, 2), s -> s.setValue(HeaterBlock.ACTIVE, false), false);
        scene.world().modifyBlock(util.grid().at(2, 3, 2), s -> s.setValue(MelterBlock.ACTIVE, false), false);

        scene.idle(5);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(1, 3, 2), Direction.NORTH), Pointing.DOWN, 30).rightClick();

        scene.overlay().showText(40)
                        .text("Right Click faucet to release fluid")
                        .placeNearTarget()
                        .pointAt(util.vector().blockSurface(util.grid().at(1, 3, 2), Direction.NORTH))
                        .attachKeyFrame();

        scene.idle(5);

        scene.world().modifyBlockEntity(util.grid().at(1, 3, 2), FaucetBlockEntity.class, be -> {
            be.onActivationPacket(ironFluid, true);
        });

        scene.idle(40);

        scene.world().modifyBlockEntity(util.grid().at(1, 3, 2), FaucetBlockEntity.class, be -> {
            be.onActivationPacket(ironFluid, false);
        });

        scene.world().modifyBlockEntity(util.grid().at(1, 2, 2), CastingBlockEntity.Table.class, be -> {
            be.getTank().fill(ironFluid, EXECUTE);
        });

        scene.world().modifyBlockEntity(util.grid().at(2, 3, 2), MelterBlockEntity.class, be -> {
            be.getTank().drain(ironFluid, EXECUTE);
        });

        scene.idle(40);

        scene.world().moveSection(smelter_link, util.vector().of(0, 1, 0), 20);
        scene.world().moveSection(left_basin, util.vector().of(0, 1, 0), 20);
        scene.world().moveSection(right_basin, util.vector().of(0, 1, 0), 20);

        scene.idle(40);
        scene.world().showSection(util.select().fromTo(1, 1, 1, 3, 1, 2), Direction.NORTH);
        scene.world().showSection(util.select().fromTo(2, 4, 2, 2, 5, 2), Direction.NORTH);

        scene.idle(20);

        scene.overlay().showText(40)
                        .text("Use hoppers, or modded methods to remove the item result")
                        .pointAt(util.vector().blockSurface(util.grid().at(1, 2, 2), Direction.NORTH))
                        .placeNearTarget()
                        .attachKeyFrame();

        scene.idle(40);

        scene.overlay().showText(40)
                        .text("And to fill the melter")
                        .pointAt(util.vector().blockSurface(util.grid().at(2, 5, 2), Direction.NORTH))
                        .placeNearTarget();

        FluidStack ironFluid2 = new FluidStack(TinkerFluids.moltenIron.getStill(), 810);

        scene.world().modifyBlockEntity(util.grid().at(2, 3, 2), MelterBlockEntity.class, be -> {
            be.getTank().setFluid(ironFluid2);
        });

        scene.idle(40);

        scene.rotateCameraY(180);

        scene.world().showSection(util.select().fromTo(1, 3, 3, 3, 3, 3), Direction.DOWN);
        scene.idle(20);

        scene.overlay().showText(40)
                        .text("Use redstone to automatically drain the faucets")
                        .pointAt(util.vector().blockSurface(util.grid().at(3, 4, 4), Direction.EAST))
                        .placeNearTarget()
                        .attachKeyFrame();
        scene.idle(40);

        scene.world().toggleRedstonePower(util.select().fromTo(3, 3, 3, 3, 3, 3));
        scene.world().modifyBlockEntity(util.grid().at(3, 3, 2), FaucetBlockEntity.class, be -> {
            be.onActivationPacket(ironFluid2, true);
        });
        scene.idle(40);
        scene.world().modifyBlockEntity(util.grid().at(3, 2, 2), CastingBlockEntity.Basin.class, be -> {
            be.getTank().fill(ironFluid2, EXECUTE);
        });
        scene.world().modifyBlockEntity(util.grid().at(2, 3, 2), MelterBlockEntity.class, be -> {
            be.getTank().drain(ironFluid2, EXECUTE);
        });
        scene.world().modifyBlockEntity(util.grid().at(3, 3, 2), FaucetBlockEntity.class, be -> {
            be.onActivationPacket(ironFluid2, false);
        });
        scene.idle(20);

        scene.rotateCameraY(180);

        scene.idle(20);

        scene.markAsFinished();
    }

    public static void recyclingBlock(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("recycling_block", BocLang.translateDirect("ponder.recycling_block.header").getString());
        scene.showBasePlate();

        var recycler = scene.world().showIndependentSection(util.select().position(2, 2, 3), Direction.DOWN);
        scene.world().moveSection(recycler, util.vector().of(0, -1, 0), 0);

        scene.markAsFinished();
    }
}
