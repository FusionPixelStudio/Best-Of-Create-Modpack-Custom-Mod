//package net.AsherRoland.BoCCustom.client.gui.quests;
//
//import at.petrak.hexcasting.xplat.IXplatAbstractions;
//import dev.ftb.mods.ftblibrary.ui.Button;
//import dev.ftb.mods.ftblibrary.ui.Panel;
//import dev.ftb.mods.ftblibrary.ui.Theme;
//import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
//import dev.ftb.mods.ftblibrary.ui.Widget;
//import dev.ftb.mods.ftblibrary.ui.misc.AbstractButtonListScreen;
//import dev.ftb.mods.ftbquests.client.ClientQuestFile;
//import dev.ftb.mods.ftbquests.client.gui.FTBQuestsTheme;
//import dev.ftb.mods.ftbquests.quest.task.Task;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CastSpellSelectScreen extends AbstractButtonListScreen {
//
//    private final Task task;
//    private ResourceLocation selectedSpell = null;
//
//    public CastSpellSelectScreen(Task task) {
//        this.task = task;
//        setTitle(Component.literal("Select a Hex Spell"));
//        setHasSearchBox(true);
//    }
//
//    @Override
//    public void addButtons(Panel panel) {
//        getAllHexSpells().forEach(spellId -> {
//            panel.add(new SpellButton(panel, spellId));
//        });
//    }
//
//    private List<ResourceLocation> getAllHexSpells() {
//        return IXplatAbstractions.INSTANCE.getActionRegistry()
//                .keySet()
//                .stream()
////                .map(ResourceKey::location)
//                .sorted()
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    protected void doAccept() {
//        if (selectedSpell != null) {
//            task.setSpellId(selectedSpell);
//        }
//        onBack();
//    }
//
//    @Override
//    protected void doCancel() {
//        onBack();
//    }
//
//    @Override
//    protected int getTopPanelHeight() {
//        return 40;
//    }
//
//    @Override
//    protected ButtonPanel createMainPanel() {
//        return new ButtonPanel();
//    }
//
//    @Override
//    public Theme getTheme() {
//        return FTBQuestsTheme.INSTANCE;
//    }
//
//    @Override
//    public boolean doesGuiPauseGame() {
//        return ClientQuestFile.exists() && ClientQuestFile.INSTANCE.isPauseGame();
//    }
//
//    private class SpellButton extends Button {
//        private final ResourceLocation spellId;
//
//        public SpellButton(Panel panel, ResourceLocation spellId) {
//            super(panel);
//            this.spellId = spellId;
//        }
//
//        @Override
//        public void onClicked(MouseButton button) {
//            selectedSpell = spellId;
//        }
//
//        @Override
//        public Component getTitle() {
//            return Component.literal(spellId.toString());
//        }
//    }
//}