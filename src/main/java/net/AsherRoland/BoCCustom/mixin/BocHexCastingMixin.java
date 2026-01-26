package net.AsherRoland.BoCCustom.mixin;

import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.AsherRoland.BoCCustom.task.CastSpellTask;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(OperatorSideEffect.AttemptSpell.class)
public abstract class BocHexCastingMixin {

    @Inject(method = "performEffect", at = @At("TAIL"))
    private void boc$onHexCast(CastingVM harness, CallbackInfo ci) {
        // The spell object is inside the AttemptSpell instance
        RenderedSpell spell = ((OperatorSideEffect.AttemptSpell)(Object)this).spell;

        // If you only want to track one spell, you can check class:
        // if (!(spell instanceof BreakBlockSpell)) return;

        if (!(harness.env.castingEntity instanceof ServerPlayer player)) return;
        if (player.level().isClientSide) return;

        // NOTE: RenderedSpell doesn't give you a ResourceLocation directly.
        // So you must use something else like the class name.
        ResourceLocation spellId = getSpellId(spell);

        if (spellId == null) return;

        castSpell(player, spellId);
    }

    private ResourceLocation getSpellId(RenderedSpell spell) {
        // This is the key part:
        // Hexcasting does NOT provide a spell ID, so you must use the class name.

        String className = spell.getClass().getSimpleName();

        // Convert to a ResourceLocation like:
        // hexcasting:break_block_spell
        // You can adjust this format however you want.

        return new ResourceLocation("hexcasting", className.toLowerCase());
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
