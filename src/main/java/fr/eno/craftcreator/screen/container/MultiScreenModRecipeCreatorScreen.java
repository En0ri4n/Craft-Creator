package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.kubejs.managers.RecipeManagerDispatcher;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.screen.widgets.NumberDataFieldWidget;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class MultiScreenModRecipeCreatorScreen<T extends CommonContainer> extends TaggeableSlotsContainerScreen<T>
{
    int guiTextureSize = 256;

    protected ExecuteButton executeButton;
    protected SimpleButton nextButton;
    protected SimpleButton previousButton;

    protected final List<NumberDataFieldWidget> dataFields;

    protected final RecipeInfos recipeInfos;

    protected int currentScreenIndex;

    public MultiScreenModRecipeCreatorScreen(T screenContainer, Inventory inv, Component titleIn, BlockPos pos)
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
        assert this.minecraft != null;

        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("screen_index", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.INT));

        this.addRenderableWidget(executeButton = new ExecuteButton(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35, 42, (button) -> RecipeManagerDispatcher.createRecipe(this.getMenu().getMod(), getCurrentRecipe(), this.getMenu().slots.stream().filter(slot -> slot instanceof SimpleSlotItemHandler).collect(Collectors.toList()), getRecipeInfos())));

        this.addRenderableWidget(nextButton = new SimpleButton(References.getTranslate("screen.recipe_creator.button.next"), getArrowXPos(true), this.topPos + this.imageHeight - 66, 10, 20, (button) -> nextPage()));
        this.addRenderableWidget(previousButton = new SimpleButton(References.getTranslate("screen.recipe_creator.button.previous"), getArrowXPos(false), this.topPos + this.imageHeight - 66, 10, 20, (button) -> previousPage()));
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        if(dataName.equals("screen_index"))
        {
            this.setCurrentScreenIndex((Integer) data);
        }
    }

    public int getArrowXPos(boolean right)
    {
        return right ? this.leftPos + this.imageWidth + 3 : this.leftPos - 3 - 10;
    }

    protected RecipeInfos getRecipeInfos()
    {
        this.recipeInfos.getParameters().clear();

        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterMap<>("tagged_slots", this.getTagged()));
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterIntList("nbt_slots", this.nbtSlots));
        return this.recipeInfos;
    }

    private Map<Integer, ResourceLocation> getTagged()
    {
        Map<Integer, ResourceLocation> map = new HashMap<>();

        this.getTaggedSlots().forEach((slot, loc) -> map.put(slot.getSlotIndex(), loc));

        return map;
    }

    /**
     * This method is called when the arrow buttons are pressed.
     * It is used to change the screen. To Override.
     */
    protected void updateScreen()
    {
        hideDataFields();
        this.updateSlots();

        nextButton.visible = hasNext();
        previousButton.visible = hasPrevious();
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

    protected void setDataFieldValue(Number defaultValue, int... index)
    {
        for(int i : index)
        {
            this.dataFields.get(i).setNumberValue(defaultValue);
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

    protected void setDataField(int index, int x, int y, int width, Object value)
    {
        setDataFieldPosAndSize(index, x, y, width);
        dataFields.get(index).setValue(String.valueOf(value));
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
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        assert minecraft != null;

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.dataFields.forEach(field ->
        {
            if(field.visible) field.render(matrixStack, mouseX, mouseY, partialTicks);
        });
    }

    @Override
    protected void renderComponentHoverEffect(PoseStack pPoseStack, @Nullable Style pStyle, int pMouseX, int pMouseY)
    {
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
        super.renderComponentHoverEffect(pPoseStack, pStyle, pMouseX, pMouseY);
    }

    protected void renderDataFieldAndTitle(int index, Component fieldTitle, PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.dataFields.get(index).render(matrixStack, mouseX, mouseY, partialTicks);
        renderDataFieldTitle(index, fieldTitle, matrixStack);
    }

    protected void renderDataFieldTitle(int index, Component fieldTitle, PoseStack matrixStack)
    {
        matrixStack.pushPose();
        float scale = 1F;
        matrixStack.scale(scale, scale, scale);
        Screen.drawString(matrixStack, font, fieldTitle.copy().withStyle(ChatFormatting.ITALIC), (int) (dataFields.get(index).x / scale), (int) ((dataFields.get(index).y - font.lineHeight * scale - 1) / scale), 0xFF88AEC1);
        matrixStack.popPose();
    }

    protected NumberDataFieldWidget getNumberField(int index)
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
        if(Character.isDigit(codePoint) || codePoint == '.')
            this.dataFields.forEach(field -> field.charTyped(codePoint, modifiers));
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.dataFields.forEach(field -> field.mouseClicked(mouseX, mouseY, button));
        boolean flag = super.mouseClicked(mouseX, mouseY, button);
        updateScreen();
        return flag;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float pPartialTick, int pMouseX, int pMouseY)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getCurrentRecipe().getGuiTexture());
        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.guiTextureSize, this.guiTextureSize);

        super.renderBg(matrixStack, pPartialTick, pMouseX, pMouseY);
    }

    protected abstract Item getRecipeIcon();

    protected PairValues<Integer, Integer> getIconPos()
    {
        return PairValues.create(0, 0);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY)
    {
        if(getRecipeIcon() != Items.AIR)
            getItemRenderer().renderAndDecorateFakeItem(new ItemStack(getRecipeIcon()), getIconPos().getFirstValue(), getIconPos().getSecondValue());
        super.renderTooltip(poseStack, mouseX, mouseY);
        this.dataFields.forEach(field -> field.renderToolTip(poseStack, mouseX, mouseY));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int pMouseX, int pMouseY)
    {
        assert minecraft != null;
        drawCenteredString(matrixStack, minecraft.font, References.getTranslate("screen." + this.getMenu().getMod().getModId() + "_recipe_creator." + RecipeFileUtils.getName(getCurrentRecipe().getRecipeType()).getPath() + ".title"), this.imageWidth / 2, -15, 0xFFFFFF);
        super.renderLabels(matrixStack, pMouseX, pMouseY);
    }

    /**
     * @param index       the index of the slot IN the available recipe slots
     * @param name        name to render
     * @param matrixStack the matrix stack
     */
    protected void renderSlotTitle(int index, Component name, PoseStack matrixStack)
    {
        SimpleSlotItemHandler slot = getCurrentSlot(index);
        Screen.drawCenteredString(matrixStack, font, name.copy().withStyle(ChatFormatting.DARK_PURPLE), this.leftPos + slot.x + 8, this.topPos + slot.y - font.lineHeight - 1, 0xFFFFFFFF);
    }

    /**
     * @param index the index of the slot IN the available recipe slots
     * @return the slot at the given index
     */
    protected SimpleSlotItemHandler getCurrentSlot(int index)
    {
        return (SimpleSlotItemHandler) PositionnedSlot.getSlotsFor(getCurrentRecipe().getSlots(), this.getMenu().slots).get(index);
    }

    protected ItemRenderer getItemRenderer()
    {
        assert minecraft != null;
        return minecraft.getItemRenderer();
    }

    private boolean hasNext()
    {
        return currentScreenIndex < getAvailableRecipesCreator().size() - 1;
    }

    private void nextPage()
    {
        if(!hasNext()) return;

        this.currentScreenIndex++;
        updateServerIndex();
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
        updateServerIndex();
        updateScreen();
    }

    protected void addNumberField(int x, int y, int width, Number defaultValue, int count)
    {
        for(int i = 0; i < count; i++)
        {
            NumberDataFieldWidget numberDataFieldWidget = new NumberDataFieldWidget(this.font, x, y, width, 10, defaultValue);
            numberDataFieldWidget.setValue(String.valueOf(defaultValue));
            this.dataFields.add(numberDataFieldWidget);
        }
    }

    private void updateServerIndex()
    {
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("screen_index", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.INT, this.currentScreenIndex));
    }

    protected void updateSlots()
    {
        this.getMenu().activeSlots(false);
        this.getCurrentRecipe().getSlots().forEach(ds -> this.getMenu().slots.stream().filter(s -> s.getSlotIndex() == ds.getIndex() && s instanceof SimpleSlotItemHandler).findFirst().ifPresent(slot -> ((SimpleSlotItemHandler) slot).setActive(true)));
    }
}