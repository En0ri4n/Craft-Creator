package fr.eno.craftcreator.screen.widgets;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.screen.widgets.buttons.IconButton;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.EntryHelper;
import fr.eno.craftcreator.utils.JsonSerializable;
import fr.eno.craftcreator.utils.PairValues;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeEntryWidget
{
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private final boolean isOutput;
    private int maxEntry;
    private boolean hasCount;
    private boolean hasTag;
    private boolean hasChance;

    private RecipeCreator recipeCreator;
    private final BlockPos tilePos;

    private DropdownListWidget<RecipeEntryEntry> entriesDropdown;

    private SuggesterTextFieldWidget registryNameField;
    private NumberDataFieldWidget countField;
    private SimpleCheckBox tagCheckBox;
    private NumberDataFieldWidget chanceField;

    private IconButton removeEntryButton;
    private IconButton saveEntryButton;
    private IconButton resetEntriesButton;

    // Used to display the recipe summary in the tooltip
    private final ItemStack recipeSummaryStack = new ItemStack(Items.KNOWLEDGE_BOOK);

    private ItemStack displayStack = ItemStack.EMPTY;
    private int displayStackPosX;
    private int displayStackPosY;
    private int displayCounter;

    private IFormattableTextComponent message;
    private int messageCounter;
    private boolean canUseWidget;

    public RecipeEntryWidget(RecipeCreator recipeCreator, BlockPos pos, int x, int y, int width, int height, boolean isOutput, int maxEntry)
    {
        this.recipeCreator = recipeCreator;
        this.tilePos = pos;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isOutput = isOutput;
        this.maxEntry = maxEntry;
        this.hasCount = true;
        this.hasTag = true;
        this.hasChance = true;
        init();
    }

    private void init()
    {
        int startY = this.y + 3;
        int gapX = 3;
        int startX = this.x + gapX;
        int y = 16 + 3;
        int i = 0;

        int itemSlotSize = 16;
        this.displayStackPosX = x + 3;
        this.displayStackPosY = this.y + height - 3 - itemSlotSize;

        ArrayList<RecipeEntryEntry> entries = new ArrayList<>();
        entries.add(new RecipeEntryEntry(false));
        if(maxEntry > 1 || maxEntry == -1) // Add a second entry if the max entry is greater than 1 (because the last entry is always a "add new entry" entry)
            entries.add(new RecipeEntryEntry(true));

        this.entriesDropdown = new DropdownListWidget<>(startX, startY + y * i++, width - 6, 16, 16, entries, (entry) ->
        {
            if(entry.isLast() && (getMaxEntry() == -1 || entriesDropdown.getDropdownEntries().size() < getMaxEntry() + 1))
            {
                RecipeEntryEntry newEntry = new RecipeEntryEntry(false);
                if(maxEntry != -1 && entriesDropdown.getDropdownEntries().size() == maxEntry) // If the max entry is reached, the last entry is replaced by a simple entry
                    entriesDropdown.setEntry(newEntry, entry);
                else
                    entriesDropdown.insertEntryBefore(newEntry, entry);
                entriesDropdown.setDropdownSelected(newEntry);

                this.registryNameField.setValue(newEntry.getRegistryName().toString());
                this.countField.setNumberValue(newEntry.getCount(), false);
                this.tagCheckBox.setSelected(newEntry.isTag());
                this.chanceField.setNumberValue(newEntry.getChance(), true);
                this.removeEntryButton.active = true;
                entriesDropdown.trimWidthToEntries();
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
        this.tagCheckBox = new SimpleCheckBox(startX, startY + y * i++, 10, 10, References.getTranslate("screen.widget.recipe_entry_widget.tag"), new StringTextComponent(""), false, true, checkbox ->
        {
            this.registryNameField.setEntries(EntryHelper.getStringEntryListWith(isTag() ? EntryHelper.getTags() : EntryHelper.getItems(), isTag() ? SimpleListWidget.ResourceLocationEntry.Type.TAG : SimpleListWidget.ResourceLocationEntry.Type.ITEM), true);
            this.registryNameField.setValue("");
        });

        if(isOutput) // Hide tag checkbox for output and set it to false to ensure that the output is not a tag
        {
            this.tagCheckBox.visible = false;
            this.tagCheckBox.setSelected(false);
        }

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
                updateServerEntries();
            }

            checkButtons();
        });

        // Save Button
        this.saveEntryButton = new IconButton(x + width - 21 - 21, startY + height - 24, References.getLoc("textures/gui/icons/save.png"), 16, 16, 16, 48, b ->
        {
            this.entriesDropdown.getDropdownSelected().set(CommonUtils.parse(registryNameField.getValue()), countField.getIntValue(), isTag(), chanceField.getDoubleValue());
            this.entriesDropdown.trimWidthToEntries();
            showMessage(References.getTranslate("screen.widget.dropdown_list.entry.saved").withStyle(TextFormatting.GREEN), 2);
            updateServerEntries();
            checkButtons();
        });

        this.resetEntriesButton = new IconButton(x + width - 21 - 21 - 21, startY + height - 24, References.getLoc("textures/gui/icons/cross.png"), 16, 16, 16, 48, b ->
        {
            reset();
            showMessage(References.getTranslate("screen.widget.dropdown_list.entry.reset").withStyle(TextFormatting.RED), 2);
            updateServerEntries();
            checkButtons();
        });

        this.saveEntryButton.active = false;
    }

    private void updateServerEntries()
    {
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket(isOutput ? "outputs" : "inputs", tilePos, InitPackets.PacketDataType.PAIR_VALUE_STRING_JSON_OBJECT_LIST, PairValues.create(recipeCreator.getRecipeTypeLocation().getPath(), this.entriesDropdown.getDropdownEntries().stream().filter(ree -> !ree.isEmpty()).map(RecipeEntryEntry::serialize).collect(Collectors.toList()))));
    }

    public void refresh(RecipeCreator recipeCreator)
    {
        this.setRecipeCreator(recipeCreator);

        reset();

        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket((isOutput ? "outputs-" : "inputs-") + recipeCreator.getRecipeTypeLocation().getPath(), tilePos, InitPackets.PacketDataType.PAIR_VALUE_STRING_JSON_OBJECT_LIST));
    }

    private void setRecipeCreator(RecipeCreator recipeCreator)
    {
        this.recipeCreator = recipeCreator;
    }

    public void setEntries(List<JsonObject> jsonList)
    {
        if(jsonList.isEmpty())
        {
            reset();
            return;
        }

        this.entriesDropdown.getDropdownEntries().clear();
        ArrayList<RecipeEntryEntry> list = jsonList.stream().map(RecipeEntryEntry::deserialize).collect(Collectors.toCollection(ArrayList::new));
        if(maxEntry > 1 || maxEntry == -1)
            list.add(new RecipeEntryEntry(true));
        this.entriesDropdown.setEntries(list);
        this.entriesDropdown.trimWidthToEntries();
        checkButtons();
    }

    private void reset()
    {
        ArrayList<RecipeEntryEntry> entries = new ArrayList<>();
        entries.add(new RecipeEntryEntry(false));
        if(maxEntry > 1 || maxEntry == -1)
            entries.add(new RecipeEntryEntry(true));
        this.entriesDropdown.getDropdownEntries().clear();
        this.entriesDropdown.setEntries(entries);
        displayStack = ItemStack.EMPTY;
        registryNameField.setValue(Items.AIR.getRegistryName().toString());
        this.entriesDropdown.trimWidthToEntries();
        checkButtons();
    }

    protected void checkButtons()
    {
        this.removeEntryButton.active = entriesDropdown.getEntries().size() > 2;
        this.resetEntriesButton.active = entriesDropdown.getDropdownEntries().stream().anyMatch(ree -> !ree.isEmpty());
        this.registryNameField.setEntries(EntryHelper.getStringEntryListWith(isTag() ? EntryHelper.getTags() : EntryHelper.getItems(), isTag() ? SimpleListWidget.ResourceLocationEntry.Type.TAG : SimpleListWidget.ResourceLocationEntry.Type.ITEM), true);
    }

    public int getMaxEntry()
    {
        return maxEntry;
    }

    public void setMaxSize(int maxEntry)
    {
        this.maxEntry = maxEntry;
    }

    public void tick()
    {
        // Update widgets visibility
        this.tagCheckBox.visible = hasTag();
        this.countField.visible = hasCount();
        this.chanceField.visible = hasChance();

        registryNameField.tick();

        // Update message counter
        if(messageCounter > 0)
            messageCounter--;

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

    public void renderTooltip(MatrixStack poseStack, int mouseX, int mouseY, int screenWidth, int screenHeight)
    {
        List<IFormattableTextComponent> tooltip = new ArrayList<>();

        tooltip.add(References.getTranslate("screen.widget.simple_list.tooltip." + (isOutput ? "output" : "input")));
        entriesDropdown.getDropdownEntries().forEach(ree ->
        {
            IFormattableTextComponent base = new StringTextComponent(TextFormatting.BLUE + (ree.isTag() ? "Tag" : "Item"));
            base.append(new StringTextComponent(TextFormatting.WHITE + " : "));
            base.append(new StringTextComponent(TextFormatting.DARK_AQUA + ree.getRegistryName().toString()));
            base.append(new StringTextComponent(TextFormatting.GRAY + String.format(" (x%d) ", ree.getCount())));
            if(ree.getChance() != 1D)
                base.append(new StringTextComponent(TextFormatting.DARK_GRAY + String.format("%.1f", 100 * ree.getChance()) + "%"));
            if(!ree.isEmpty()) tooltip.add(base);
        });

        int itemSlotX = x + 3;
        int itemSlotY = y + height - 3 - 16;
        if(ScreenUtils.isMouseHover(itemSlotX + 16 + 6, itemSlotY, mouseX, mouseY, 16, 16))
            GuiUtils.drawHoveringText(poseStack, tooltip, mouseX, mouseY, screenWidth, screenHeight, -1, ClientUtils.getFontRenderer());

        if(resetEntriesButton.isMouseOver(mouseX, mouseY))
            GuiUtils.drawHoveringText(poseStack, Collections.singletonList(References.getTranslate("screen.widget.recipe_entry_widget.tooltip.reset")), mouseX, mouseY, screenWidth, screenHeight, -1, ClientUtils.getFontRenderer());
        if(removeEntryButton.isMouseOver(mouseX, mouseY))
            GuiUtils.drawHoveringText(poseStack, Collections.singletonList(References.getTranslate("screen.widget.recipe_entry_widget.tooltip.remove")), mouseX, mouseY, screenWidth, screenHeight, -1, ClientUtils.getFontRenderer());
        if(saveEntryButton.isMouseOver(mouseX, mouseY))
            GuiUtils.drawHoveringText(poseStack, Collections.singletonList(References.getTranslate("screen.widget.recipe_entry_widget.tooltip.save")), mouseX, mouseY, screenWidth, screenHeight, -1, ClientUtils.getFontRenderer());
        if(tagCheckBox.isMouseOver(mouseX, mouseY))
            GuiUtils.drawHoveringText(poseStack, Collections.singletonList(References.getTranslate("screen.widget.recipe_entry_widget.tooltip.tag")), mouseX, mouseY, screenWidth, screenHeight, -1, ClientUtils.getFontRenderer());
    }

    /**
     * Show a message for a given time
     *
     * @param message The message to show
     * @param time    The time in seconds
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
        Screen.fill(matrixStack, displayStackPosX, displayStackPosY, displayStackPosX + 16, displayStackPosY + 16, 0x68000000);

        ClientUtils.getItemRenderer().renderGuiItem(displayStack, displayStackPosX, displayStackPosY);
        ClientUtils.getItemRenderer().renderGuiItem(recipeSummaryStack, displayStackPosX + 16 + 6, displayStackPosY);
        RenderSystem.pushMatrix();
        if(registryNameField.isFocused())
            RenderSystem.translatef(0, 0, -300); // Minecraft renders items decorations with a z level of ~200, so we need to decrease it
        ClientUtils.getItemRenderer().renderGuiItemDecorations(ClientUtils.getFontRenderer(), displayStack, displayStackPosX, displayStackPosY, null);
        RenderSystem.popMatrix();


        countField.render(matrixStack, mouseX, mouseY, partialTicks);
        tagCheckBox.render(matrixStack, mouseX, mouseY, partialTicks);
        chanceField.render(matrixStack, mouseX, mouseY, partialTicks);
        removeEntryButton.render(matrixStack, mouseX, mouseY, partialTicks);
        saveEntryButton.render(matrixStack, mouseX, mouseY, partialTicks);
        resetEntriesButton.render(matrixStack, mouseX, mouseY, partialTicks);

        if(!displayStack.isEmpty() && ScreenUtils.isMouseHover(displayStackPosX, displayStackPosY, mouseX, mouseY, 16, 16))
            ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, ClientUtils.getCurrentScreen().getTooltipFromItem(displayStack), mouseX, mouseY);

        if(messageCounter > 0 && message != null)
        {
            ClientUtils.getFontRenderer().draw(matrixStack, message, x, y + height, 0xFFFFFFFF);
        }
    }

    public void renderDropdown(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        registryNameField.render(matrixStack, mouseX, mouseY, partialTicks);
        entriesDropdown.render(matrixStack, mouseX, mouseY, partialTicks);

        ItemStack stack = ClientUtils.getClientPlayer().inventory.getCarried();

        if(!stack.isEmpty())
        {
            // Render the item being carried because it's not rendered on top of the widget
            // Yes, we can call that hardcoding, but it's working and it's not that bad (I think)
            // But if you have a better solution, please tell me
            RenderSystem.pushMatrix(); // Fortunately, in next version of forge, a matrix stack will not be created for each frame by this method
            RenderSystem.translatef(0, 0, 300); // On top of everything
            ClientUtils.getItemRenderer().renderGuiItem(stack, mouseX - 8, mouseY - 8);
            ClientUtils.getItemRenderer().renderGuiItemDecorations(ClientUtils.getFontRenderer(), stack, mouseX - 8, mouseY - 8, null);
            RenderSystem.popMatrix();
        }
    }

    public void setCanUseWidget(boolean canUseWidget)
    {
        this.canUseWidget = canUseWidget;
    }

    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if(canUseWidget)
        {
            if(!entriesDropdown.isFocused())
            {
                if(!registryNameField.isFocused())
                {
                    countField.mouseClicked(mouseX, mouseY, button);
                    tagCheckBox.mouseClicked(mouseX, mouseY, button);
                    chanceField.mouseClicked(mouseX, mouseY, button);
                    removeEntryButton.mouseClicked(mouseX, mouseY, button);
                    saveEntryButton.mouseClicked(mouseX, mouseY, button);
                    resetEntriesButton.mouseClicked(mouseX, mouseY, button);

                    if(ScreenUtils.isMouseHover(displayStackPosX, displayStackPosY, (int) mouseX, (int) mouseY, 16, 16))
                    {
                        ItemStack stack = ClientUtils.getClientPlayer().inventory.getCarried();

                        if(!stack.isEmpty())
                        {
                            displayStack = new ItemStack(stack.getItem());
                            registryNameField.setValue(stack.getItem().getRegistryName().toString());
                            countField.setNumberValue(countField.visible ? stack.getCount() : 1, false);
                            chanceField.setNumberValue(1D, true);
                            tagCheckBox.setSelected(false);
                        }
                    }
                }

                registryNameField.mouseClicked(mouseX, mouseY, button);
            }

            entriesDropdown.mouseClicked(mouseX, mouseY, button);
        }
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

    public boolean hasCount()
    {
        return hasCount;
    }

    public void setHasCount(boolean hasCount)
    {
        this.hasCount = hasCount;
    }

    public boolean hasTag()
    {
        return hasTag;
    }

    public void setHasTag(boolean hasTag)
    {
        this.hasTag = hasTag;
    }

    public boolean hasChance()
    {
        return hasChance;
    }

    public void setHasChance(boolean hasChance)
    {
        this.hasChance = hasChance;
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
            pMatrixStack.pushPose();
            pMatrixStack.translate(0, 0, 100); // Make sure text is rendered on top of the item
            Screen.drawCenteredString(pMatrixStack, ClientUtils.getFontRenderer(), getDisplayName(pIndex), pLeft + pWidth / 2, pTop + 3, 0x000000);
            pMatrixStack.popPose();
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
                    if(displayCounter / displayTime >= tag.getValues().size()) displayCounter = 0;

                    Item item = tag.getValues().get(displayCounter / displayTime);
                    if(item != Items.AIR && item != null)
                    {
                        displayStack = new ItemStack(item);
                    }

                    if(!Screen.hasShiftDown()) displayCounter++;
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

        public boolean isEmpty()
        {
            return this.registryName.equals(Items.AIR.getRegistryName());
        }

        public boolean isLast()
        {
            return isLast;
        }
    }
}
