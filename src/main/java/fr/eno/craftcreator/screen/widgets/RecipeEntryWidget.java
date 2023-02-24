package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.EntryHelper;
import fr.eno.craftcreator.utils.NBTSerializable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class RecipeEntryWidget
{
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private DropdownListWidget<RecipeEntryEntry> entriesDropdown;

    private SuggesterTextFieldWidget registryNameField;
    private NumberDataFieldWidget countField;
    private SimpleCheckBox tagCheckBox;
    private NumberDataFieldWidget chanceField;

    private IconButton removeEntryButton;
    private IconButton saveEntryButton;

    public RecipeEntryWidget(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        init();
    }

    private void init()
    {
        int startY = this.y + 3;
        int gapX = 3;
        int startX = this.x + gapX;
        int y = 16 + 3;
        int i = 0;

        ArrayList<RecipeEntryEntry> entries = new ArrayList<>();
        entries.add(new RecipeEntryEntry(false));
        entries.add(new RecipeEntryEntry(false));
        entries.add(new RecipeEntryEntry(true));
        this.entriesDropdown = new DropdownListWidget<>(startX, startY + y * i++, width - 6, 16, 16, entries, (entry) ->
        {
            //TODO: Updates current displayed data

            if(entriesDropdown.getIndex(entry) == entriesDropdown.getEntries().size() - 1)
            {
                RecipeEntryEntry newEntry = new RecipeEntryEntry(false);
                entriesDropdown.insertEntryBefore(newEntry, entry);
                entriesDropdown.setDropdownSelected(newEntry);

                this.registryNameField.setValue(newEntry.getRegistryName().toString());
                this.countField.setNumberValue(newEntry.getCount(), false);
                this.tagCheckBox.setSelected(newEntry.isTag());
                this.chanceField.setNumberValue(newEntry.getChance(), true);
                this.removeEntryButton.active = true;
                return;
            }

            this.tagCheckBox.setSelected(entry.isTag());
            this.registryNameField.setValue(entry.getRegistryName().toString());
            this.countField.setNumberValue(entry.getCount(), false);
            this.chanceField.setNumberValue(entry.getChance(), true);
        });

        // Registry Name Text Field
        this.registryNameField = new SuggesterTextFieldWidget(startX, startY + y * i++, width - 2 * gapX, 16, EntryHelper.getStringEntryListWith(EntryHelper.getItems()), null, (newValue) ->
                this.saveEntryButton.active = CommonUtils.parse(this.registryNameField.getValue()) != null && CommonUtils.getItem(CommonUtils.parse(this.registryNameField.getValue())) != null || CommonUtils.getTag(CommonUtils.parse(this.registryNameField.getValue())) != null);
        this.registryNameField.setVisible(true);
        this.registryNameField.setBlitOffset(100);

        // Tag Checkbox
        this.tagCheckBox = new SimpleCheckBox(startX, startY + y * i++, 10, 10, new TextComponent("is tag"), new TextComponent(""), false, true, checkbox ->
        {
            this.registryNameField.setEntries(EntryHelper.getStringEntryListWith(checkbox.selected() ? EntryHelper.getTags() : EntryHelper.getItems()), true);
            this.registryNameField.setValue("");
        });

        int fieldsWidth = 30;

        // Count Text Field
        this.countField = new NumberDataFieldWidget(startX + width - fieldsWidth - gapX * 2, startY + (y = 14 + 3) * i++, fieldsWidth, 12, new TextComponent("Count :"), 1, false);

        // Chance Text Field
        this.chanceField = new NumberDataFieldWidget(startX + width - fieldsWidth - gapX * 2, startY + y * i++, fieldsWidth, 12, new TextComponent("Chance :"), 1D, true);

        // Remove button
        this.removeEntryButton = new IconButton(x + width - 3 - 16, startY + height - 20, References.getLoc("textures/gui/icons/trash_can.png"), 16, 16, 16, 48, b -> {
            if(entriesDropdown.getEntries().size() > 2)
            {
                this.entriesDropdown.removeEntry(entriesDropdown.getDropdownSelected());
                this.entriesDropdown.setScrollAmount(0D);
            }
            else
            {
                this.removeEntryButton.active = false;
            }
        });

        // Save Button
        this.saveEntryButton = new IconButton(x + width - 3 - 16 - 3 - 16, startY + height - 20, References.getLoc("textures/gui/icons/save.png"), 16, 16, 16, 48, b -> {
            this.entriesDropdown.getDropdownSelected().set(CommonUtils.parse(registryNameField.getValue()), countField.getIntValue(), tagCheckBox.selected(), chanceField.getDoubleValue());
            System.out.println("Save entry : Name=" + registryNameField.getValue() + " isTag=" + tagCheckBox.selected() + " count=" + countField.getValue() + " chance=" + chanceField.getValue());
        });
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        Screen.fill(matrixStack, x, y, x + width, y + height, 0x88000000);

        countField.render(matrixStack, mouseX, mouseY, partialTicks);
        tagCheckBox.render(matrixStack, mouseX, mouseY, partialTicks);
        chanceField.render(matrixStack, mouseX, mouseY, partialTicks);
        removeEntryButton.render(matrixStack, mouseX, mouseY, partialTicks);
        saveEntryButton.render(matrixStack, mouseX, mouseY, partialTicks);
        registryNameField.render(matrixStack, mouseX, mouseY, partialTicks);
        entriesDropdown.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if(!entriesDropdown.isFocused())
        {
            registryNameField.mouseClicked(mouseX, mouseY, button);
            countField.mouseClicked(mouseX, mouseY, button);
            tagCheckBox.mouseClicked(mouseX, mouseY, button);
            chanceField.mouseClicked(mouseX, mouseY, button);
            removeEntryButton.mouseClicked(mouseX, mouseY, button);
            saveEntryButton.mouseClicked(mouseX, mouseY, button);
        }

        entriesDropdown.mouseClicked(mouseX, mouseY, button);
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers)
    {
        registryNameField.keyPressed(keyCode, scanCode, modifiers);
        countField.keyPressed(keyCode, scanCode, modifiers);
        tagCheckBox.keyPressed(keyCode, scanCode, modifiers);
        chanceField.keyPressed(keyCode, scanCode, modifiers);
    }

    public void charTyped(char codePoint, int modifiers)
    {
        registryNameField.charTyped(codePoint, modifiers);
        countField.charTyped(codePoint, modifiers);
        tagCheckBox.charTyped(codePoint, modifiers);
        chanceField.charTyped(codePoint, modifiers);
    }

    public void mouseScrolled(double mouseX, double mouseY, double delta)
    {
        entriesDropdown.mouseScrolled(mouseX, mouseY, delta);
        registryNameField.mouseScrolled(mouseX, mouseY, delta);
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
    {
        entriesDropdown.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        registryNameField.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean isFocused()
    {
        return entriesDropdown.isFocused() || registryNameField.isFocused();
    }

    public static class RecipeEntryEntry extends DropdownListWidget.Entry<ResourceLocation> implements NBTSerializable<RecipeEntryEntry>
    {
        private final boolean isLast;
        private ResourceLocation registryName;
        private int count;
        private boolean isTag;
        private double chance;

        public RecipeEntryEntry(boolean isLast)
        {
            this.isLast = isLast;
            this.registryName = CommonUtils.parse("minecraft:air");
            this.count = 1;
            this.isTag = false;
            this.chance = 1D;
        }

        @Override
        public ResourceLocation getValue()
        {
            return registryName;
        }

        @Override
        public MutableComponent getDisplayName(int index)
        {
            return References.getTranslate(getTranslationKey(), index + 1);
        }

        private String getTranslationKey()
        {
            return isLast ? "screen.widget.dropdown_list.recipe_entry.add" : "screen.widget.dropdown_list.recipe_entry.entry.title";
        }

        @Override
        public void render(PoseStack pMatrixStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTicks)
        {
            Screen.fill(pMatrixStack, pLeft, pTop, pLeft + pWidth - 4, pTop + pHeight, 0x88FFFFFF);
            ClientUtils.getFontRenderer().draw(pMatrixStack, getDisplayName(pIndex), pLeft + 3, pTop + 3, 0x000000);
        }

        public void set(ResourceLocation registryName, int count, boolean isTag, double chance)
        {
            this.registryName = registryName;
            this.count = count;
            this.isTag = isTag;
            this.chance = chance;
        }

        public ResourceLocation getRegistryName()
        {
            return registryName;
        }

        public int getCount()
        {
            return count;
        }

        public boolean isTag()
        {
            return isTag;
        }

        public double getChance()
        {
            return chance;
        }

        @Override
        public CompoundTag serialize()
        {
            CompoundTag compound = new CompoundTag();
            compound.putString("RegistryName", registryName.toString());
            compound.putInt("Count", count);
            compound.putBoolean("IsTag", isTag);
            compound.putDouble("Chance", chance);
            return compound;
        }

        @Override
        public RecipeEntryEntry deserialize(CompoundTag compound)
        {
            this.registryName = CommonUtils.parse(compound.getString("RegistryName"));
            this.count = compound.getInt("Count");
            this.isTag = compound.getBoolean("IsTag");
            this.chance = compound.getDouble("Chance");
            return this;
        }

        @Override
        public String getEntryValue()
        {
            return this.registryName.toString();
        }

        @Override
        public void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY)
        {

        }
    }
}
