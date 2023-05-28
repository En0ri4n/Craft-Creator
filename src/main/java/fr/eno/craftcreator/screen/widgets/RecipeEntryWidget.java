package fr.eno.craftcreator.screen.widgets;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.screen.widgets.buttons.IconButton;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.EntryHelper;
import fr.eno.craftcreator.utils.JsonSerializable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class RecipeEntryWidget
{
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private SlotItemHandler linkedSlot;
    private ItemStack lastStack = ItemStack.EMPTY;

    private DropdownListWidget<RecipeEntryEntry> entriesDropdown;

    private SuggesterTextFieldWidget registryNameField;
    private NumberDataFieldWidget countField;
    private SimpleCheckBox tagCheckBox;
    private NumberDataFieldWidget chanceField;

    private IconButton removeEntryButton;
    private IconButton saveEntryButton;

    private ItemStack displayStack = ItemStack.EMPTY;
    private int displayCounter;

    private IFormattableTextComponent message;
    private int messageCounter;

    public RecipeEntryWidget(SlotItemHandler linkedSlot, int x, int y, int width, int height)
    {
        this.linkedSlot = linkedSlot;
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
        this.registryNameField = new SuggesterTextFieldWidget(startX, startY + y * i++, width - 2 * gapX, 16, EntryHelper.getStringEntryListWith(EntryHelper.getItems(), SimpleListWidget.ResourceLocationEntry.Type.ITEM), null, (newValue) ->
        {
            this.saveEntryButton.active = CommonUtils.getItem(CommonUtils.parse(newValue)) != Items.AIR || CommonUtils.getTag(CommonUtils.parse(newValue)) != null;
        });
        this.registryNameField.setVisible(true);
        this.registryNameField.setBlitOffset(100);

        // Tag Checkbox
        this.tagCheckBox = new SimpleCheckBox(startX, startY + y * i++, 10, 10, new StringTextComponent("is tag"), new StringTextComponent(""), false, true, checkbox ->
        {
            this.registryNameField.setEntries(EntryHelper.getStringEntryListWith(isTag() ? EntryHelper.getTags() : EntryHelper.getItems(), isTag() ? SimpleListWidget.ResourceLocationEntry.Type.TAG : SimpleListWidget.ResourceLocationEntry.Type.ITEM), true);
            this.registryNameField.setValue("");
        });

        int fieldsWidth = 30;

        // Count Text Field
        this.countField = new NumberDataFieldWidget(startX + width - fieldsWidth - gapX * 2, startY + (y = 14 + 3) * i++, fieldsWidth, 12, new StringTextComponent("Count :"), 1, false);

        // Chance Text Field
        this.chanceField = new NumberDataFieldWidget(startX + width - fieldsWidth - gapX * 2, startY + y * i++, fieldsWidth, 12, new StringTextComponent("Chance :"), 1D, true);

        // Remove button
        this.removeEntryButton = new IconButton(x + width - 21, startY + height - 24, References.getLoc("textures/gui/icons/trash_can.png"), 16, 16, 16, 48, b ->
        {
            if(entriesDropdown.getEntries().size() > 2)
            {
                this.entriesDropdown.removeEntry(entriesDropdown.getDropdownSelected());
                this.entriesDropdown.setScrollAmount(0D);
            }

            this.removeEntryButton.active = entriesDropdown.getEntries().size() > 2;
        });

        // Save Button
        this.saveEntryButton = new IconButton(x + width - 21 - 21, startY + height - 24, References.getLoc("textures/gui/icons/save.png"), 16, 16, 16, 48, b ->
        {
            this.entriesDropdown.getDropdownSelected().set(CommonUtils.parse(registryNameField.getValue()), countField.getIntValue(), isTag(), chanceField.getDoubleValue());
            this.entriesDropdown.trimWidthToEntries();
            showMessage(References.getTranslate("screen.widget.dropdown_list.entry.saved").withStyle(TextFormatting.GREEN), 2);
            System.out.println("Save entry : Name=" + registryNameField.getValue() + " isTag=" + isTag() + " count=" + countField.getValue() + " chance=" + chanceField.getValue());
        });
    }

    public void tick()
    {
        registryNameField.tick();

        // Update message
        if(messageCounter > 0)
        {
            messageCounter--;
        }

        // Update data if slot is not empty
        if(linkedSlot.hasItem() && lastStack != linkedSlot.getItem())
        {
            this.registryNameField.setValue(linkedSlot.getItem().getItem().getRegistryName().toString());
            this.countField.setNumberValue(linkedSlot.getItem().getCount(), false);
            this.chanceField.setNumberValue(1D, true);
            this.tagCheckBox.setSelected(false);

            this.lastStack = linkedSlot.getItem();
        }

        // Update display stack
        if(!isTag())
        {
            displayStack = new ItemStack(CommonUtils.getItem(CommonUtils.parse(registryNameField.getValue())));
        }
        else
        {
            ITag<Item> tag = CommonUtils.getTag(CommonUtils.parse(registryNameField.getValue()));

            if(tag != null)
            {
                int displayTime = 20;
                List<Item> availableItems = tag.getValues();

                if(availableItems.size() > 0)
                {
                    if(displayCounter / displayTime >= availableItems.size()) displayCounter = 0;

                    displayStack = new ItemStack(availableItems.get(displayCounter / displayTime));
                    displayCounter++;
                }
            }
            else
            {
                displayStack = ItemStack.EMPTY;
            }
        }

        displayStack.setCount(countField.getIntValue());
    }

    private static void loopItemEngine(ItemStack displayStack, String registryName, boolean isTag, int displayCounter)
    {
    }

    /**
     * Show a message for a given time
     *
     * @param message The message to show
     * @param time The time in seconds
     */
    private void showMessage(IFormattableTextComponent message, int time)
    {
        this.message = message;
        this.messageCounter = time * 20;
    }

    private boolean isTag()
    {
        return this.tagCheckBox.selected();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        Screen.fill(matrixStack, x, y, x + width, y + height, 0x88000000);

        // Render the current entry item
        int itemSlotSize = 17;
        int itemSlotX = x + 3;
        int itemSlotY = y + height - 3 - itemSlotSize;
        Screen.fill(matrixStack, itemSlotX, itemSlotY, itemSlotX + itemSlotSize, itemSlotY + itemSlotSize, 0x68000000);
        ClientUtils.getItemRenderer().renderGuiItem(displayStack, itemSlotX, itemSlotY);
        RenderSystem.pushMatrix();
        if(registryNameField.isFocused()) RenderSystem.translatef(0, 0, -300); // Minecraft renders items decorations with a z level of ~200, so we need to decrease it
        ClientUtils.getItemRenderer().renderGuiItemDecorations(ClientUtils.getFontRenderer(), displayStack, itemSlotX, itemSlotY, null);
        RenderSystem.popMatrix();

        countField.render(matrixStack, mouseX, mouseY, partialTicks);
        tagCheckBox.render(matrixStack, mouseX, mouseY, partialTicks);
        chanceField.render(matrixStack, mouseX, mouseY, partialTicks);
        removeEntryButton.render(matrixStack, mouseX, mouseY, partialTicks);
        saveEntryButton.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pushPose();
        if(registryNameField.isFocused()) matrixStack.translate(0, 0, 300); // Minecraft renders items with a z level of ~200, so we need to render the text field on top of it
        registryNameField.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();

        if(!displayStack.isEmpty() && ScreenUtils.isMouseHover(itemSlotX, itemSlotY, mouseX, mouseY, itemSlotSize, itemSlotSize))
            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, ClientUtils.getCurrentScreen().getTooltipFromItem(displayStack), mouseX, mouseY);

        if(messageCounter > 0 && message != null)
        {
            ClientUtils.getFontRenderer().draw(matrixStack, message, x, y + height, 0xFFFFFFFF);
        }

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
        return entriesDropdown.isFocused() || registryNameField.isFocused() || countField.isFocused() || chanceField.isFocused();
    }

    public void setLinkedSlot(SlotItemHandler linkedSlot)
    {
        this.linkedSlot = linkedSlot;
    }

    public static class RecipeEntryEntry extends DropdownListWidget.Entry<ResourceLocation> implements JsonSerializable
    {
        private final boolean isLast;
        private ResourceLocation registryName;
        private int count;
        private boolean isTag;
        private double chance;

        private ItemStack displayStack = ItemStack.EMPTY;
        private int displayCounter;

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
        public IFormattableTextComponent getDisplayName(int index)
        {
            return (isLast ? References.getTranslate(getTranslationKey(), index + 1) : new StringTextComponent(this.registryName.getPath())).withStyle(TextFormatting.WHITE);
        }

        private String getTranslationKey()
        {
            return isLast ? "screen.widget.dropdown_list.recipe_entry.add" : "screen.widget.dropdown_list.recipe_entry.entry.title";
        }

        @Override
        public void render(MatrixStack pMatrixStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTicks)
        {
            int yPos = 10 / 2 - 16 / 2;
            ClientUtils.getItemRenderer().renderAndDecorateFakeItem(displayStack, yPos, yPos);
            Screen.fill(pMatrixStack, pLeft, pTop, pLeft + pWidth - 4, pTop + pHeight, 0x88FFFFFF);
            ClientUtils.getFontRenderer().draw(pMatrixStack, getDisplayName(pIndex), pLeft + 3, pTop + 3, 0x000000);
        }

        @Override
        protected void tick()
        {
            if(!isTag)
            {
                displayStack = new ItemStack(CommonUtils.getItem(registryName));
            }
            else
            {
                ITag<Item> tag = CommonUtils.getTag(registryName);

                if(tag.getValues().size() > 0)
                {
                    int displayTime = 40;
                    if(displayCounter / displayTime >= tag.getValues().size())
                        displayCounter = 0;

                    Item item = tag.getValues().get(displayCounter / displayTime);
                    if(item != Items.AIR && item != null)
                    {
                        displayStack = new ItemStack(item);
                    }

                    if(!Screen.hasShiftDown())
                        displayCounter++;
                }
            }
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
        public JsonObject serialize()
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("RegistryName", registryName.toString());
            jsonObject.addProperty("Count", count);
            jsonObject.addProperty("IsTag", isTag);
            jsonObject.addProperty("Chance", chance);
            return jsonObject;
        }

        public static RecipeEntryEntry deserialize(JsonObject jsonObject)
        {
            RecipeEntryEntry recipeEntryEntry = new RecipeEntryEntry(false);
            recipeEntryEntry.registryName = CommonUtils.parse(jsonObject.get("RegistryName").getAsString());
            recipeEntryEntry.count = jsonObject.get("Count").getAsInt();
            recipeEntryEntry.isTag = jsonObject.get("IsTag").getAsBoolean();
            recipeEntryEntry.chance = jsonObject.get("Chance").getAsDouble();
            return recipeEntryEntry;
        }

        @Override
        public String getEntryValue()
        {
            return this.registryName.toString();
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {

        }
    }
}
