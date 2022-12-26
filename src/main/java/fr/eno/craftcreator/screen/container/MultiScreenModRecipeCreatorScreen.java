package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.kubejs.managers.RecipeManagerDispatcher;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.packets.GetModRecipeCreatorScreenIndexPacket;
import fr.eno.craftcreator.packets.SetModRecipeCreatorScreenIndexServerPacket;
import fr.eno.craftcreator.screen.ModRecipes;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.utils.FieldAccessor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class MultiScreenModRecipeCreatorScreen<T extends CommonContainer> extends TaggeableSlotsContainerScreen<T>
{
    private static final Field SLOT_POS_X_FIELD = ObfuscationReflectionHelper.findField(Slot.class, "f_40220_");
    private static final Field SLOT_POS_Y_FIELD = ObfuscationReflectionHelper.findField(Slot.class, "f_40221_");

    protected ExecuteButton executeButton;
    protected final List<EditBox> textFields;
    
    private int currentScreenIndex;

    public MultiScreenModRecipeCreatorScreen(T screenContainer, Inventory inv, Component titleIn, BlockPos pos)
    {
        super(screenContainer, inv, titleIn, pos);
        this.textFields = new ArrayList<>();
        this.currentScreenIndex = 0;
    }

    @Override
    protected void init()
    {
        super.init();
        assert this.minecraft != null;

        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetModRecipeCreatorScreenIndexPacket(this.getMenu().getTile().getBlockPos()));

        this.addRenderableWidget(executeButton = new ExecuteButton(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35,40, (button) -> RecipeManagerDispatcher.createRecipe(this.getMenu().getMod(), getCurrentRecipe(), this.getMenu().slots.stream().filter(slot -> slot instanceof SimpleSlotItemHandler).collect(Collectors.toList()), getTagged(), getParametersValue())));

        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.botania_recipe_creator.button.next"), this.leftPos + this.imageWidth + 3, this.topPos + this.imageHeight - 66, 10, 20, (button) -> nextPage()));
        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.botania_recipe_creator.button.previous"),  this.leftPos - 3 - 10, this.topPos + this.imageHeight - 66,10, 20, (button) -> previousPage()));

        textFields.add(new EditBox(minecraft.font, leftPos + imageWidth - 44, topPos + imageHeight / 2 - 16, 35, 10, new TextComponent("")));
        textFields.get(0).setValue(String.valueOf(100));

        updateScreen();
    }

    private List<Integer> getParametersValue()
    {
        try
        {
            return textFields.stream().map(editBox -> Integer.parseInt(editBox.getValue())).collect(Collectors.toList());
        }
        catch(NumberFormatException ignored) {}

        return Collections.singletonList(-1);
    }

    private Map<Integer, ResourceLocation> getTagged()
    {
        Map<Integer, ResourceLocation> map = new HashMap<>();

        this.getTaggedSlots().forEach((slot, loc) -> map.put(slot.getSlotIndex(), loc));

        return map;
    }

    protected abstract void updateScreen();

    public void setCurrentScreenIndex(int index)
    {
        this.currentScreenIndex = index;
        updateScreen();
    }

    protected void setTextFieldPos(int index, int x, int y)
    {
        textFields.get(index).x = x;
        textFields.get(index).y = y;
    }

    protected void setExecuteButtonPos(int x, int y)
    {
        executeButton.x = x;
        executeButton.y = y;
    }

    protected void setSlot(int index, int x, int y)
    {
        this.getMenu().slots.stream().filter(s -> s.getSlotIndex() == index && s instanceof SimpleSlotItemHandler).findFirst().ifPresent(slot -> setPos((SimpleSlotItemHandler) slot, x, y));
    }

    protected ModRecipes getCurrentRecipe()
    {
        return ModRecipes.getRecipes(this.getMenu().getMod()).get(currentScreenIndex);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        assert minecraft != null;
        this.renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        this.textFields.forEach(field ->
        {
            if(field.visible)
                field.render(matrixStack, mouseX, mouseY, partialTicks);
        });
    }

    @Override
    protected void renderComponentHoverEffect(PoseStack pPoseStack, @Nullable Style pStyle, int pMouseX, int pMouseY)
    {
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
        super.renderComponentHoverEffect(pPoseStack, pStyle, pMouseX, pMouseY);
    }

    void renderTextfieldTitle(int index, String text, PoseStack matrixStack)
    {
        matrixStack.pushPose();
        float scale = 0.85F;
        matrixStack.scale(scale, scale, scale);
        Screen.drawString(matrixStack, font, new TextComponent(text), (int) (textFields.get(index).x / scale), (int) ((textFields.get(index).y - font.lineHeight * scale) / scale), 0xFFFFFF);
        matrixStack.popPose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        this.textFields.forEach(field -> field.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(Character.isDigit(codePoint))
            this.textFields.forEach(field -> field.charTyped(codePoint, modifiers));
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.textFields.forEach(field -> field.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float pPartialTick, int pMouseX, int pMouseY)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        RenderSystem.setShaderTexture(0, getCurrentRecipe().getGuiTexture());

        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        super.renderBg(matrixStack, pPartialTick, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int pMouseX, int pMouseY)
    {
        assert minecraft != null;
        drawCenteredString(matrixStack, minecraft.font, References.getTranslate("screen.botania_recipe_creator." + RecipeFileUtils.getName(getCurrentRecipe().getRecipeType()).getPath() + ".title"), this.imageWidth / 2, -15, 0xFFFFFF);
        super.renderLabels(matrixStack, pMouseX, pMouseY);
    }

    private void setPos(SimpleSlotItemHandler slot, int x, int y)
    {
        slot.setActive(true);
        // Set x pos
        new FieldAccessor(SLOT_POS_X_FIELD).set(slot, x);
        // ReflectUtils.setField(SLOT_POS_X_FIELD, slot, x);

        // Set y pos
        new FieldAccessor(SLOT_POS_Y_FIELD).set(slot, y);
        // ReflectUtils.setField(SLOT_POS_Y_FIELD, slot, y);
    }

    private void nextPage()
    {
        this.currentScreenIndex = currentScreenIndex >= ModRecipes.values().length - 1 ? 0 : currentScreenIndex + 1;
        updateServerIndex();
        updateScreen();
    }

    private void previousPage()
    {
        this.currentScreenIndex = currentScreenIndex <= 0 ? ModRecipes.values().length - 1 : this.currentScreenIndex - 1;
        updateServerIndex();
        updateScreen();
    }

    private void updateServerIndex()
    {
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new SetModRecipeCreatorScreenIndexServerPacket(this.currentScreenIndex, this.getMenu().getTile().getBlockPos()));
    }

    void updateSlots()
    {
        this.getMenu().slots.forEach(slot -> { if(slot instanceof SimpleSlotItemHandler) ((SimpleSlotItemHandler) slot).setActive(false); });
        getCurrentRecipe().getSlots().forEach(ds -> setSlot(ds.getIndex(), ds.getxPos(), ds.getyPos()));
    }
}