package fr.eno.craftcreator.client.screen.widgets;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.client.screen.widgets.buttons.EnumButton;
import fr.eno.craftcreator.client.screen.widgets.buttons.IconButton;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.recipes.utils.EntryType;
import fr.eno.craftcreator.recipes.utils.SpecialRecipeEntry;
import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.utils.EntryHelper;
import fr.eno.craftcreator.utils.JsonSerializable;
import fr.eno.craftcreator.utils.PairValues;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.BucketItem;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeEntryWidget extends Widget
{
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private final boolean isOutput;
    private int maxEntry;
    private boolean hasFluidAmount;
    private boolean hasItemCount;
    private boolean hasChance;

    private RecipeCreator recipeCreator;
    private final BlockPos tilePos;

    private DropdownListWidget<RecipeEntryEntry> entriesDropdown;
    private SuggesterTextFieldWidget registryNameField;
    private NumberDataFieldWidget countField;
    private EnumButton<EntryType> typeButton;
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
        super(x, y, width, height, new StringTextComponent(""));
        this.recipeCreator = recipeCreator;
        this.tilePos = pos;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isOutput = isOutput;
        this.maxEntry = maxEntry;
        this.hasFluidAmount = true;
        this.hasItemCount = true;
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

                loadEntry(newEntry);
                removeEntryButton.active = true;
                entriesDropdown.trimWidthToEntries();
                return;
            }

            loadEntry(entry);
        });

        // Registry Name Text Field
        this.registryNameField = new SuggesterTextFieldWidget(startX, startY + y * i++, width - 2 * gapX, 16, EntryHelper.getStringEntryListWith(EntryHelper.getItems(), EntryType.ITEM), (newEntry) -> registryNameField.setSelected(null), (newValue) ->
        {
            this.saveEntryButton.active = CommonUtils.getItem(CommonUtils.parse(newValue)) != Items.AIR || CommonUtils.getFluid(CommonUtils.parse(newValue)) != null || !CommonUtils.getTag(CommonUtils.parse(newValue)).getValues().isEmpty();
        });
        this.registryNameField.setVisible(true);
        this.registryNameField.setBlitOffset(100);

        // Tag Checkbox
        this.typeButton = new EnumButton<>(Arrays.asList(EntryType.ITEM, EntryType.TAG, EntryType.FLUID), startX, startY + y * i++, 50, 16, 0x80000000, button ->
        {
            this.registryNameField.setEntries(EntryHelper.getStringEntryListWith(getEntries(), getType()), true);
            this.countField.setMessage(new StringTextComponent(getType() == EntryType.FLUID ? "Amount :" : "Count :"));
            // this.registryNameField.setValue(""); // Do we need to clear the text field ?
        });

        if(isOutput) // Set the available types for the output
            this.typeButton.setItems(Arrays.asList(EntryType.ITEM, EntryType.FLUID));

        int fieldsWidth = 40;

        // Count Text Field
        this.countField = new NumberDataFieldWidget(startX + width - fieldsWidth - gapX * 2, startY + (y = 14 + 4) * i++, fieldsWidth, 12, new StringTextComponent("Count :"), 1, false);

        // Chance Text Field
        this.chanceField = new NumberDataFieldWidget(startX + width - fieldsWidth - gapX * 2, startY + y * i++, fieldsWidth, 12, new StringTextComponent("Chance :"), 1D, true);

        // Remove button
        this.removeEntryButton = new IconButton(x + width - 21 - 21, startY + height - 24, References.getLoc("textures/gui/icons/trash_can.png"), 16, 16, 16, 48, b ->
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
        this.saveEntryButton = new IconButton(x + width - 21 - 21 - 21, startY + height - 24, References.getLoc("textures/gui/icons/save.png"), 16, 16, 16, 48, b ->
        {
            this.entriesDropdown.getDropdownSelected().set(CommonUtils.parse(registryNameField.getValue()), countField.getIntValue(), typeButton.getSelected(), chanceField.getDoubleValue());
            this.entriesDropdown.trimWidthToEntries();
            showMessage(References.getTranslate("screen.widget.dropdown_list.entry.saved").withStyle(TextFormatting.GREEN), 2);
            updateServerEntries();
            checkButtons();
        });

        this.resetEntriesButton = new IconButton(x + width - 21, startY + height - 24, References.getLoc("textures/gui/icons/cross.png"), 16, 16, 16, 48, b ->
        {
            reset();
            showMessage(References.getTranslate("screen.widget.dropdown_list.entry.reset").withStyle(TextFormatting.RED), 2);
            updateServerEntries();
            checkButtons();
        });

        this.saveEntryButton.active = false;
    }

    private void loadEntry(RecipeEntryEntry entry)
    {
        this.registryNameField.setValue(entry.getRegistryName().toString());
        this.countField.setNumberValue(entry.getCount(), false);
        this.typeButton.setSelected(entry.getType());
        this.chanceField.setNumberValue(entry.getChance(), true);
    }

    private List<ResourceLocation> getEntries()
    {
        if(getType().isTag())
            return EntryHelper.getTags();
        else if(getType().isItem())
            return EntryHelper.getItems();
        else if(getType().isFluid())
            return EntryHelper.getFluids();

        return new ArrayList<>();
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
        if((maxEntry > 1 || maxEntry == -1) && list.size() < getMaxEntry())
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
        registryNameField.setSelected(null);
        registryNameField.setValue(Items.AIR.getRegistryName().toString());
        this.entriesDropdown.trimWidthToEntries();
        checkButtons();
    }

    public void setAllowedTypes(EntryType... types)
    {
        this.typeButton.setItems(Arrays.asList(types));
    }

    protected void checkButtons()
    {
        this.removeEntryButton.active = entriesDropdown.getEntries().size() > 2;
        this.resetEntriesButton.active = entriesDropdown.getDropdownEntries().stream().anyMatch(ree -> !ree.isEmpty());
        this.registryNameField.setEntries(EntryHelper.getStringEntryListWith(
                getType().isTag() ?
                        EntryHelper.getTags() :
                        getType().isItem() ?
                                EntryHelper.getItems() :
                                EntryHelper.getFluids(),
                getType()), true);
        this.registryNameField.setSelected(null);
        //this.registryNameField.setSelected(registryNameField.getEntries().stream().filter(e -> e.getEntryValue().equals(entriesDropdown.getDropdownSelected().getEntryValue())).findFirst().orElse(null));
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
        this.countField.visible = getType().isFluid() ? hasFluidAmount() : hasItemCount();
        this.chanceField.visible = hasChance();

        registryNameField.tick();

        // Update message counter
        if(messageCounter > 0)
            messageCounter--;

        // Update display stack
        if(getType().isItem())
        {
            displayStack = new ItemStack(CommonUtils.getItem(CommonUtils.parse(registryNameField.getValue())));
        }
        else if(getType().isFluid())
        {
            displayStack = new ItemStack(CommonUtils.getFluid(CommonUtils.parse(registryNameField.getValue())).getBucket());
        }
        else if(getType().isTag())
        {
            ITag<Item> tag = CommonUtils.getTag(CommonUtils.parse(registryNameField.getValue()));

            if(!tag.getValues().isEmpty())
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
        entriesDropdown.getDropdownEntries().forEach(rse ->
        {
            IFormattableTextComponent base = new StringTextComponent(TextFormatting.BLUE + (rse.getRecipeEntry().isTag() ? "Tag" : rse.getRecipeEntry().isItem() ? "Item": "Fluid"));
            base.append(new StringTextComponent(TextFormatting.WHITE + " : "));
            base.append(new StringTextComponent(TextFormatting.DARK_AQUA + rse.getRegistryName().toString()));
            base.append(new StringTextComponent(TextFormatting.GRAY + String.format(getType().isFluid() ? " (%dmb) " : " (x%d) ", rse.getCount())));
            if(rse.getChance() != 1D)
                base.append(new StringTextComponent(TextFormatting.DARK_GRAY + String.format("%.1f", 100 * rse.getChance()) + "%"));
            if(!rse.isEmpty()) tooltip.add(base);
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
        if(typeButton.isMouseOver(mouseX, mouseY))
            GuiUtils.drawHoveringText(poseStack, Collections.singletonList(References.getTranslate("screen.widget.recipe_entry_widget.tooltip.type")), mouseX, mouseY, screenWidth, screenHeight, -1, ClientUtils.getFontRenderer());
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

    private EntryType getType()
    {
        return this.typeButton.getSelected();
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
        typeButton.render(matrixStack, mouseX, mouseY, partialTicks);
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
            RenderSystem.pushMatrix(); // Fortunately, in next version of forge (at least 1.18), a matrix stack will not be created for each frame by the item render method
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(canUseWidget)
        {
            if(!entriesDropdown.isFocused())
            {
                if(!registryNameField.isFocused())
                {
                    countField.mouseClicked(mouseX, mouseY, button);
                    typeButton.mouseClicked(mouseX, mouseY, button);
                    chanceField.mouseClicked(mouseX, mouseY, button);
                    removeEntryButton.mouseClicked(mouseX, mouseY, button);
                    saveEntryButton.mouseClicked(mouseX, mouseY, button);
                    resetEntriesButton.mouseClicked(mouseX, mouseY, button);

                    // Check if the player is hovering the display slot with a an item in hand and if so, copy it to the widget
                    if(ScreenUtils.isMouseHover(displayStackPosX, displayStackPosY, (int) mouseX, (int) mouseY, 16, 16))
                    {
                        ItemStack stack = ClientUtils.getClientPlayer().inventory.getCarried();

                        if(!stack.isEmpty())
                        {
                            displayStack = new ItemStack(stack.getItem());
                            typeButton.setSelected(stack.getItem() instanceof BucketItem ? EntryType.FLUID : EntryType.ITEM);
                            registryNameField.setValue(stack.getItem().getRegistryName().toString());
                            if(countField.visible)
                                countField.setNumberValue(typeButton.getSelected().isFluid() ? 1000 : stack.getCount(), false);
                            chanceField.setNumberValue(1D, true);
                        }
                    }
                }

                registryNameField.mouseClicked(mouseX, mouseY, button);
            }

            entriesDropdown.mouseClicked(mouseX, mouseY, button);
        }

        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        registryNameField.keyPressed(keyCode, scanCode, modifiers);
        countField.keyPressed(keyCode, scanCode, modifiers);
        chanceField.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char codePoint, int modifiers)
    {
        registryNameField.charTyped(codePoint, modifiers);
        countField.charTyped(codePoint, modifiers);
        chanceField.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        entriesDropdown.mouseScrolled(mouseX, mouseY, delta);
        registryNameField.mouseScrolled(mouseX, mouseY, delta);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
    {
        entriesDropdown.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        registryNameField.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean isFocused()
    {
        return entriesDropdown.isFocused() || registryNameField.isFocused() || countField.isFocused() || chanceField.isFocused();
    }

    public boolean hasItemCount()
    {
        return hasItemCount;
    }

    public void setHasItemCount(boolean hasItemCount)
    {
        this.hasItemCount = hasItemCount;
    }

    public void setHasFluidAmount(boolean hasFluidAmount)
    {
        this.hasFluidAmount = hasFluidAmount;
    }

    public boolean hasFluidAmount()
    {
        return hasFluidAmount;
    }

    public void setHasCount(boolean hasCount)
    {
        this.hasItemCount = hasCount;
        this.hasFluidAmount = hasCount;
    }

    public boolean hasChance()
    {
        return hasChance;
    }

    public void setHasChance(boolean hasChance)
    {
        this.hasChance = hasChance;
    }

    private static class RecipeEntryEntry extends DropdownListWidget.Entry<ResourceLocation> implements JsonSerializable
    {
        private final boolean isLast;
        private final SpecialRecipeEntry recipeEntry;

        private ItemStack displayStack = ItemStack.EMPTY;
        private int displayCounter;

        public RecipeEntryEntry(boolean isLast)
        {
            this.isLast = isLast;
            this.recipeEntry = new SpecialRecipeEntry();
        }

        public RecipeEntryEntry(boolean isLast, SpecialRecipeEntry recipeEntry)
        {
            this.isLast = isLast;
            this.recipeEntry = recipeEntry;
        }

        @Override
        public ResourceLocation getValue()
        {
            return recipeEntry.getRegistryName();
        }

        public SpecialRecipeEntry getRecipeEntry()
        {
            return recipeEntry;
        }

        @Override
        public IFormattableTextComponent getDisplayName(int index)
        {
            return (isLast ? References.getTranslate(getTranslationKey(), index + 1) : new StringTextComponent(recipeEntry.getRegistryName().getPath())).withStyle(TextFormatting.WHITE);
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
            IFormattableTextComponent name = getDisplayName(pIndex);
            if(!isLast())
                name.append(TextFormatting.YELLOW + String.valueOf(TextFormatting.ITALIC) + " (" + (getType().isItem() || getType().isTag() ? "x" + recipeEntry.getCount() : recipeEntry.getCount() + "mb") + ")");

            Screen.drawCenteredString(pMatrixStack, ClientUtils.getFontRenderer(), name, pLeft + pWidth / 2, pTop + 3, 0x000000);
            pMatrixStack.popPose();
        }

        @Override
        protected void tick()
        {
            if(!recipeEntry.isTag())
            {
                displayStack = new ItemStack(CommonUtils.getItem(recipeEntry.getRegistryName()));
            }
            else
            {
                ITag<Item> tag = CommonUtils.getTag(recipeEntry.getRegistryName());

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

        public void set(ResourceLocation registryName, int count, EntryType type, double chance)
        {
            if(registryName.getPath().isEmpty()) registryName = new ResourceLocation("minecraft", "air");
            recipeEntry.setRegistryName(registryName);
            recipeEntry.setCount(count);
            recipeEntry.setType(type);
            recipeEntry.setChance(chance);
        }

        public void setType(EntryType type)
        {
            recipeEntry.setType(type);
        }

        public ResourceLocation getRegistryName()
        {
            return recipeEntry.getRegistryName();
        }

        public int getCount()
        {
            return recipeEntry.getCount();
        }

        public EntryType getType()
        {
            return recipeEntry.getType();
        }

        public double getChance()
        {
            return recipeEntry.getChance();
        }

        @Override
        public JsonObject serialize()
        {
            return recipeEntry.serialize();
        }

        public static RecipeEntryEntry deserialize(JsonObject jsonObject)
        {
            return new RecipeEntryEntry(false, SpecialRecipeEntry.deserialize(jsonObject));
        }

        @Override
        public String getEntryValue()
        {
            return this.recipeEntry.getRegistryName().toString();
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {

        }

        public boolean isEmpty()
        {
            return recipeEntry.isEmpty();
        }

        public boolean isLast()
        {
            return isLast;
        }
    }
}
