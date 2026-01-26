package net.AsherRoland.BoCCustom.task;

import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.EnumConfig;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.AbstractBooleanTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.AsherRoland.BoCCustom.BocCustom;
//import net.AsherRoland.BoCCustom.client.gui.quests.CastSpellSelectScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CastSpellTask extends AbstractBooleanTask {

    private ResourceLocation spellId = new ResourceLocation("hexcasting", "edify");

    public ResourceLocation getSpellId() {
        return spellId;
    }

    public CastSpellTask(long id, Quest quest) {
        super(id, quest);
        spellId = getSpellId();
    }

    public void setSpellId(ResourceLocation id) {
        this.spellId = id;
    }

    private Component getSpellName(ResourceLocation spellId) {
        // Hexcasting uses translation keys like:
        // hexcasting.spell.<spellname>
        return Component.translatable("hexcasting.spell." + spellId.getPath());
    }

//    @Override
//    public void onButtonClicked(Button button, boolean canClick) {
//        new CastSpellSelectScreen(this).openGui();
//    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("spell")) {
            spellId = new ResourceLocation(tag.getString("spell"));
        }
    }

    @Override
    public void writeData(CompoundTag tag) {
        super.writeData(tag);
        if (spellId != null) {
            tag.putString("spell", spellId.toString());
        }
    }

    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);

        List<String> spellList = getAllHexSpells()
                .stream()
                .map(ResourceLocation::toString)
                .sorted()
                .collect(Collectors.toList());

        String current = spellId.toString();

        NameMap<String> nameMap = NameMap.of(current, spellList)
                .id(s -> s)
                .name(s -> Component.literal(s))
                .create();

        config.addEnum("spell",
                spellId.toString(),
                v -> spellId = new ResourceLocation(v),
                nameMap
        ).setNameKey("boc_custom.task.spell");
    }

    private List<ResourceLocation> getAllHexSpells() {
        return IXplatAbstractions.INSTANCE.getActionRegistry()
                .keySet()
                .stream()
                .filter(id -> id.getNamespace().equals("hexcasting"))
                .collect(Collectors.toList());
    }

    @Override
    public MutableComponent getAltTitle() {
        return Component.literal("").append(spellId.toString());
    }

    @Override
    public Icon getAltIcon() {
        return ItemIcon.getIcon("minecraft:item/amethyst_shard");
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        return false;
    }

    @Override
    public TaskType getType() {
        return BocCustom.HEX_CASTING;
    }

    public void progress(TeamData teamData, ResourceLocation key, long value, boolean ignore) {
        if (!teamData.isCompleted(this)) {
            if (ignore) {
                teamData.addProgress(this, value);
            } else if (checkTaskSequence(teamData) && teamData.canStartTasks(getQuest())) {
                teamData.addProgress(this, value);
            }
        }
    }
}
