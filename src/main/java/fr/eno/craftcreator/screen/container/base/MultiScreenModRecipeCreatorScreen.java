package fr.eno.craftcreator.screen.container.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.base.RecipeManagerDispatcher;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.widgets.ButtonGrid;
import fr.eno.craftcreator.screen.widgets.NumberDataFieldWidget;
import fr.eno.craftcreator.screen.widgets.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.widgets.buttons.IconButton;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleButton;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleCheckBox;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class MultiScreenModRecipeCreatorScreen<T extends CommonContainer> extends TaggeableSlotsContainerScreen<T>
{
    protected int guiTextureSize = 256;
    
    // Only to check if it's a vanilla machine
    protected boolean isVanillaScreen;
    private SimpleCheckBox isKubeJSRecipeButton;
    
    protected ExecuteButton executeButton;
    protected SimpleButton nextButton;
    protected SimpleButton previousButton;
    
    protected ButtonGrid<IconButton> buttonGrid;
    protected IconButton recipeTypeButton;
    
    protected final List<NumberDataFieldWidget> dataFields;
    
    protected final RecipeInfos recipeInfos;
    
    protected int currentScreenIndex;
    
    public MultiScreenModRecipeCreatorScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, BlockPos pos)
    {
        super(screenContainer, inv, titleIn, pos);
        this.dataFields = new ArrayList<>();
        this.currentScreenIndex = 0;
        this.recipeInfos = RecipeInfos.create();
    }
    
    @Override
    protected void init()
    {
        super.init();
        
        this.buttonGrid = new ButtonGrid<>(this.leftPos + this.imageWidth, this.topPos, 20, 2, 4, IconButton.getButtons(getAvailableRecipesCreator().stream().map(this::getRecipeIcon).collect(Collectors.toList())), (button) ->
        {
            this.currentScreenIndex = this.buttonGrid.getButtons().indexOf(button);
            updateData();
            updateScreen();
            
            this.buttonGrid.setVisible(false);
            this.recipeTypeButton.setItem(button.getItem());
        });
        
        addButton(this.recipeTypeButton = new IconButton(this.leftPos + imageWidth - 20, this.topPos, ItemStack.EMPTY, button -> this.buttonGrid.setVisible(!this.buttonGrid.isVisible())));
        
        if(isVanillaScreen)
        {
            InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("kubejs_recipe", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN));
            this.addButton(isKubeJSRecipeButton = new SimpleCheckBox(leftPos, topPos + imageHeight + 2, 10, 10, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));
            if(!SupportedMods.isKubeJSLoaded())
            {
                isKubeJSRecipeButton.visible = false;
                isKubeJSRecipeButton.setSelected(false);
            }
        }
        
        this.addButton(executeButton = new ExecuteButton(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35, 42, (button) -> RecipeManagerDispatcher.createRecipe(this.getMenu().getMod(), getCurrentRecipe(),
                this.getMenu().slots.stream().filter(slot -> slot instanceof SimpleSlotItemHandler).collect(Collectors.toList()), getRecipeInfos(), getCurrentSerializerType())));
        
        this.addButton(nextButton = new SimpleButton(References.getTranslate("screen.recipe_creator.button.next"), getArrowXPos(true), this.topPos + this.imageHeight - 66, 10, 20, (button) -> nextPage()));
        this.addButton(previousButton = new SimpleButton(References.getTranslate("screen.recipe_creator.button.previous"), getArrowXPos(false), this.topPos + this.imageHeight - 66, 10, 20, (button) -> previousPage()));

        // Retrieve data from server
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("screen_index", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.INT));
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("recipe_type", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.STRING));
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("fields", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.DOUBLE_ARRAY));
    }

    protected ModRecipeSerializer.SerializerType getCurrentSerializerType()
    {
        if(isVanillaScreen && !isKubeJSRecipeButton.selected() || !SupportedMods.isKubeJSLoaded()) return ModRecipeSerializer.SerializerType.MINECRAFT_DATAPACK;
        
        return ModRecipeSerializer.SerializerType.KUBE_JS;
    }
    
    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);
        
        if(dataName.equals("screen_index")) this.setCurrentScreenIndex((Integer) data);
        else if(dataName.equals("kubejs_recipe") && isVanillaScreen) this.isKubeJSRecipeButton.setSelected((boolean) data);
        else if(dataName.equals("fields")) this.setFields((double[]) data);
    }

    private void setFields(double[] data)
    {
        for(int i = 0; i < data.length; i++)
        {
            setDataFieldValue(data[i], getDataField(i).isDouble(), i);
        }
    }

    private double[] getFields()
    {
        return this.dataFields.stream().map(NumberDataFieldWidget::getValue).mapToDouble(str -> str.isEmpty() ? 1D : Double.parseDouble(str)).toArray();
    }

    public List<Rectangle2d> getExtraAreas()
    {
        List<Rectangle2d> areas = new ArrayList<>();
        areas.add(guiTagList.getArea());
        areas.add(buttonGrid.getArea());
        return areas;
    }
    
    public int getArrowXPos(boolean right)
    {
        return right ? this.leftPos + this.imageWidth + 3 : this.leftPos - 3 - 10;
    }
    
    protected RecipeInfos getRecipeInfos()
    {
        this.recipeInfos.getParameters().clear();
        
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterMap<>(RecipeInfos.Parameters.TAGGED_SLOTS, this.getTagged()));
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterList<>(RecipeInfos.Parameters.NBT_SLOTS, this.nbtSlots));
        if(isVanillaScreen) this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean(RecipeInfos.Parameters.KUBEJS_RECIPE, this.isKubeJSRecipeButton.selected() && SupportedMods.isKubeJSLoaded()));
        return this.recipeInfos;
    }
    
    private Map<Integer, ResourceLocation> getTagged()
    {
        Map<Integer, ResourceLocation> map = new HashMap<>();
        
        this.getTaggedSlots().forEach((slot, loc) -> map.put(slot.getSlotIndex(), loc));
        
        return map;
    }
    
    /**
     * This method is called when the screen change.<br>
     * To Override to perform changes between screens.
     */
    protected void updateScreen()
    {
        hideDataFields();
        this.updateSlots();
        
        nextButton.visible = hasNext();
        previousButton.visible = hasPrevious();
        
        this.recipeTypeButton.setItem(getRecipeIcon(getCurrentRecipe()));
    }
    
    protected void hideDataFields()
    {
        this.dataFields.forEach(textField -> textField.active = textField.visible = false);
    }
    
    protected void showDataField(int... index)
    {
        for(int i : index)
        {
            this.dataFields.get(i).active = this.dataFields.get(i).visible = true;
        }
    }
    
    protected void setDataFieldValue(Number defaultValue, boolean isDouble, int... index)
    {
        for(int i : index)
        {
            this.dataFields.get(i).setNumberValue(defaultValue, isDouble);
        }
    }
    
    public void setCurrentScreenIndex(int index)
    {
        this.currentScreenIndex = index;
        updateScreen();
    }
    
    protected void setDataFieldPos(int index, int x, int y)
    {
        dataFields.get(index).x = x;
        dataFields.get(index).y = y;
    }
    
    protected void setDataFieldSize(int index, int width)
    {
        dataFields.get(index).setWidth(width);
        dataFields.get(index).setHeight(16);
    }
    
    protected void setDataFieldPosAndSize(int index, int x, int y, int width)
    {
        setDataFieldPos(index, x, y);
        setDataFieldSize(index, width);
    }
    
    protected void setDataField(int index, int x, int y, int width, Number value, boolean isDouble)
    {
        setDataFieldPosAndSize(index, x, y, width);
        setDataFieldValue(value, isDouble, index);
    }

    protected void setDataFieldTooltip(int index, IFormattableTextComponent tooltip)
    {
        getDataField(index).setTooltip(tooltip);
    }
    
    protected void setExecuteButtonPos(int x, int y)
    {
        executeButton.x = x;
        executeButton.y = y;
    }
    
    protected ModRecipeCreator getCurrentRecipe()
    {
        return getAvailableRecipesCreator().get(currentScreenIndex);
    }
    
    protected List<ModRecipeCreator> getAvailableRecipesCreator()
    {
        return ModRecipeCreator.getRecipeCreatorScreens(this.getMenu().getMod());
    }
    
    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        this.dataFields.forEach(field ->
        {
            if(field.visible) field.render(matrixStack, mouseX, mouseY, partialTicks);
        });
        
        this.buttonGrid.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void renderComponentHoverEffect(MatrixStack pPoseStack, Style pStyle, int pMouseX, int pMouseY)
    {
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
        super.renderComponentHoverEffect(pPoseStack, pStyle, pMouseX, pMouseY);
    }
    
    protected void renderDataFieldAndTitle(int index, ITextComponent fieldTitle, MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.dataFields.get(index).render(matrixStack, mouseX, mouseY, partialTicks);
        renderDataFieldTitle(index, fieldTitle, matrixStack);
    }
    
    protected void renderDataFieldTitle(int index, ITextComponent fieldTitle, MatrixStack matrixStack)
    {
        matrixStack.pushPose();
        float scale = 1F;
        matrixStack.scale(scale, scale, scale);
        Screen.drawString(matrixStack, font, fieldTitle.copy().withStyle(TextFormatting.ITALIC), (int) (dataFields.get(index).x / scale), (int) ((dataFields.get(index).y - font.lineHeight * scale - 1) / scale), 0xFF88AEC1);
        matrixStack.popPose();
    }
    
    protected NumberDataFieldWidget getDataField(int index)
    {
        return this.dataFields.get(index);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        this.dataFields.forEach(field -> field.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        this.dataFields.forEach(field -> field.charTyped(codePoint, modifiers));
        return super.charTyped(codePoint, modifiers);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.dataFields.forEach(field -> field.mouseClicked(mouseX, mouseY, button));
        if(!this.buttonGrid.isMouseOver(mouseX, mouseY)) this.buttonGrid.setVisible(false);
        else this.buttonGrid.onMouseClicked(mouseX, mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        this.fillGradient(matrixStack, 0, 0, this.width, this.height, -1072689136, -804253680);
        ClientUtils.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture(getCurrentRecipe().getGuiTexture());
        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.guiTextureSize, this.guiTextureSize);
        renderBackground(matrixStack);
    }
    
    protected abstract Item getRecipeIcon(ModRecipeCreator modRecipeCreator);
    
    @Override
    protected void renderTooltip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        super.renderTooltip(poseStack, mouseX, mouseY);
        this.dataFields.forEach(field -> field.renderToolTip(poseStack, mouseX, mouseY));
        this.recipeTypeButton.renderToolTip(poseStack, mouseX, mouseY);
        if(this.buttonGrid.isVisible()) this.buttonGrid.getButtons().forEach(button -> button.renderToolTip(poseStack, mouseX, mouseY));
    }
    
    @Override
    protected void renderLabels(MatrixStack matrixStack, int pMouseX, int pMouseY)
    {
        drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), References.getTranslate("screen." + this.getMenu().getMod().getModId() + "_recipe_creator." + CommonUtils.getRecipeTypeName(getCurrentRecipe().getRecipeType()).getPath() + ".title"), this.imageWidth / 2, -15, 0xFFFFFF);
        if(Screen.hasShiftDown() || Screen.hasControlDown()) drawString(matrixStack, this.font, References.getTranslate("screen.crafting.info.msg").getString(), 0, this.imageHeight + (isVanillaScreen ? 15 : 0), 0x707370);
    }
    
    /**
     * @param index       the index of the slot IN the available recipe slots
     * @param name        name to render
     * @param matrixStack the matrix stack
     */
    protected void renderSlotTitle(int index, ITextComponent name, MatrixStack matrixStack)
    {
        SimpleSlotItemHandler slot = getCurrentSlot(index);
        Screen.drawCenteredString(matrixStack, font, name.copy().withStyle(TextFormatting.DARK_PURPLE), this.leftPos + slot.x + 8, this.topPos + slot.y - font.lineHeight - 1, 0xFFFFFFFF);
    }
    
    /**
     * @param index the index of the slot IN the available recipe slots
     * @return the slot at the given index
     */
    protected SimpleSlotItemHandler getCurrentSlot(int index)
    {
        return (SimpleSlotItemHandler) PositionnedSlot.getSlotsFor(getCurrentRecipe().getSlots(), this.getMenu().slots).get(index);
    }
    
    private boolean hasNext()
    {
        return currentScreenIndex < getAvailableRecipesCreator().size() - 1;
    }
    
    private void nextPage()
    {
        if(!hasNext()) return;
        
        this.currentScreenIndex++;
        updateData();
        updateScreen();
    }
    
    private boolean hasPrevious()
    {
        return currentScreenIndex > 0;
    }
    
    private void previousPage()
    {
        if(!hasPrevious()) return;
        
        this.currentScreenIndex--;
        updateData();
        updateScreen();
    }
    
    protected void addNumberField(int x, int y, int width, Number defaultValue, int count)
    {
        for(int i = 0; i < count; i++)
        {
            NumberDataFieldWidget numberDataFieldWidget = new NumberDataFieldWidget(this.font, x, y, width, 10, defaultValue, defaultValue instanceof Double);
            numberDataFieldWidget.setValue(String.valueOf(defaultValue));
            this.dataFields.add(numberDataFieldWidget);
        }
    }
    
    private void updateData()
    {
        updateClientData();
        updateServerData();
    }
    
    private void updateClientData()
    {
        this.getMenu().getTile().setScreenIndex(this.currentScreenIndex);
        this.getMenu().getTile().setCurrentRecipeType(CommonUtils.getRecipeTypeName(getCurrentRecipe().getRecipeType()));
    }
    
    protected void updateServerData()
    {
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("screen_index", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.INT, this.currentScreenIndex));
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("recipe_type", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.STRING, CommonUtils.getRecipeTypeName(getCurrentRecipe().getRecipeType()).toString()));
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("fields", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.DOUBLE_ARRAY, getFields()));
        if(isVanillaScreen) InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("kubejs_recipe", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN, this.isKubeJSRecipeButton.selected()));
    }
    
    protected void updateSlots()
    {
        this.getMenu().activeSlots(false);
        this.getCurrentRecipe().getSlots().forEach(ds -> this.getMenu().slots.stream().filter(s -> s.getSlotIndex() == ds.getIndex() && s instanceof SimpleSlotItemHandler).findFirst().ifPresent(slot -> ((SimpleSlotItemHandler) slot).setActive(true)));
    }

    @Override
    public void onClose()
    {
        super.onClose();

        updateServerData();
    }
}