package net.AsherRoland.BoCCustom.mixin;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.PatternShapeMatch;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.common.casting.PatternRegistryManifest;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.AsherRoland.BoCCustom.task.CastSpellTask;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(PatternIota.class)
public abstract class BocHexCastingMixin {

    @Inject(
            method = "execute",
            at = @At(
                    value = "RETURN",
                    ordinal = 0 // ONLY the successful CastResult
            )
    )
    private void boc$afterSuccessfulCast(
            CastingVM vm,
            ServerLevel world,
            SpellContinuation continuation,
            CallbackInfoReturnable<CastResult> cir
    ) {
        if (world.isClientSide) return;

        LivingEntity caster = vm.getEnv().getCastingEntity();
        if (!(caster instanceof ServerPlayer player)) return;

        // Re-resolve the pattern to get the Action key
        PatternShapeMatch lookup = PatternRegistryManifest.matchPattern(
                ((PatternIota)(Object)this).getPattern(),
                vm.getEnv(),
                false
        );

        ResourceKey<ActionRegistryEntry> key = null;

        if (lookup instanceof PatternShapeMatch.Normal normal) {
            key = normal.key;
        } else if (lookup instanceof PatternShapeMatch.PerWorld perWorld) {
            key = perWorld.key;
        }

        if (key == null) return;

        ResourceLocation spellId = key.location();
        castSpell(player, spellId);
    }

    private void castSpell(ServerPlayer player, ResourceLocation spellId) {
        TeamData teamData = TeamData.get(player);
        if (teamData == null) return;

        for (Chapter chapter : teamData.getFile().getAllChapters()) {
            for (Quest quest : chapter.getQuests()) {
                if (!teamData.canStartTasks(quest)) continue;

                for (Task task : quest.getTasks()) {
                    if (!(task instanceof CastSpellTask castTask)) continue;

                    if (!castTask.getSpellId().equals(spellId)) continue;

                    castTask.progress(teamData, spellId, 1, false);
                }
            }
        }
    }
}
