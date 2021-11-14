package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.systems.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.kubejs.managers.*;
import fr.eno.craftcreator.kubejs.utils.*;
import fr.eno.craftcreator.packets.*;
import fr.eno.craftcreator.screen.buttons.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.network.*;
import net.minecraftforge.items.*;
import vazkii.botania.common.block.*;
import vazkii.botania.common.crafting.*;
import vazkii.botania.common.item.*;

import javax.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

@SuppressWarnings("unused")
public class BotaniaRecipeCreatorScreen extends TaggeableSlotsContainerScreen<BotaniaRecipeCreatorContainer>
{
    private final Field slotXPosField = ReflectUtils.getFieldAndSetAccessible(Slot.class, "field_75223_e");
    private final Field slotYPosField = ReflectUtils.getFieldAndSetAccessible(Slot.class, "field_75221_f");

    private TextFieldWidget textField;

    private ExecuteButton executeButton;

    private int currentScreenIndex;

    public BotaniaRecipeCreatorScreen(BotaniaRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.tile.getPos());
        this.currentScreenIndex = 0;
    }

    @Override
    protected void init()
    {
        super.init();

        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetBotaniaRecipeCreatorScreenIndexPacket(this.container.tile.getPos()));

        this.addButton(executeButton = new ExecuteButton(this.guiLeft + this.xSize / 2 - 20, this.guiTop + 35,40, (button) -> BotaniaRecipeManagers.createRecipe(getCurrentRecipe(), this.container.inventorySlots.stream().filter(slot -> slot instanceof SlotItemHandler).collect(Collectors.toList()), getTagged(), getParameterValue())));

        this.addButton(new SimpleButton(References.getTranslate("screen.botania_recipe_creator.button.next"), this.guiLeft + this.xSize + 3, this.guiTop + this.ySize - 66, 10, 20, (button) -> nextPage()));
        this.addButton(new SimpleButton(References.getTranslate("screen.botania_recipe_creator.button.previous"),  this.guiLeft - 3 - 10, this.guiTop + this.ySize - 66,10, 20, (button) -> previousPage()));

        textField = new TextFieldWidget(minecraft.fontRenderer, guiLeft + xSize - 44, guiTop + ySize / 2 - 16, 35, 10, new StringTextComponent(""));
        textField.setText(String.valueOf(100));

        updateScreen();
    }

    private int getParameterValue()
    {
        try
        {
            return Integer.parseInt(this.textField.getText());
        }
        catch(NumberFormatException ignored) {}

        return 100;
    }

    private Map<Integer, ResourceLocation> getTagged()
    {
        Map<Integer, ResourceLocation> map = new HashMap<>();

        this.getTaggedSlots().forEach((slot, loc) -> map.put(slot.getSlotIndex(), loc));

        return map;
    }

    private void updateScreen()
    {
        this.updateSlots();

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION:
                setTextFieldPos(guiLeft + xSize - 44, textField.y);
                setExecuteButtonPos(this.guiLeft + this.xSize / 2 - 20, this.guiTop + 35);
                break;
            case ELVEN_TRADE:
            case PURE_DAISY:
                setTextFieldPos(guiLeft + xSize / 2 - 35 / 2, textField.y);
                setExecuteButtonPos(this.guiLeft + this.xSize / 2 - 20, this.guiTop + 35);
                break;
            case BREWERY:
            case TERRA_PLATE:
                setTextFieldPos(guiLeft + xSize / 2, textField.y);
                setExecuteButtonPos(this.guiLeft + this.xSize / 2 - 5, this.guiTop + 31);
                break;
            case PETAL_APOTHECARY:
                setExecuteButtonPos(this.guiLeft + this.xSize / 2 - 1, this.guiTop + 31);
                break;
            case RUNIC_ALTAR:
                setTextFieldPos(guiLeft + xSize / 2, textField.y);
                setExecuteButtonPos(this.guiLeft + this.xSize / 2 - 2, this.guiTop + 31);
                break;
        }
    }

    public void setCurrentScreenIndex(int index)
    {
        this.currentScreenIndex = index;
        updateScreen();
    }

    private void setTextFieldPos(int x, int y)
    {
        textField.x = x;
        textField.y = y;
    }

    private void setExecuteButtonPos(int x, int y)
    {
        executeButton.x = x;
        executeButton.y = y;
    }

    private void setSlot(int index, int x, int y)
    {
        setPos(this.container.inventorySlots.stream().filter(s -> s.getSlotIndex() == index).findFirst().orElse(null), x, y);
    }

    private BotaniaRecipes getCurrentRecipe()
    {
        return BotaniaRecipes.values()[currentScreenIndex];
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION:
                minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModBlocks.creativePool), this.guiLeft + xSize / 2 - 8, this.guiTop + 14);
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Mana :", matrixStack);
                break;
            case ELVEN_TRADE:
                minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModBlocks.livingwoodGlimmering), this.guiLeft + xSize / 2 - 8, this.guiTop + 14);
                break;
            case PURE_DAISY:
                int i = 0;
                for(int x = 0; x < 3; x++)
                    for(int y = 0; y < 3; y++)
                    {
                        if(i == 4)
                        {
                            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModSubtiles.pureDaisy), this.guiLeft + 8 + 18 * x, this.guiTop + 18 + y * 18);
                            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModSubtiles.pureDaisy), this.guiLeft + 116 + 18 * x, this.guiTop + 18 + y * 18);
                        }
                        else if(i != 5)
                        {
                            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(this.container.inventorySlots.get(SlotHelper.PURE_DAISY_SLOTS.get(0).getIndex()).getHasStack() ? this.container.inventorySlots.get(SlotHelper.PURE_DAISY_SLOTS.get(0).getIndex()).getStack() : ItemStack.EMPTY, this.guiLeft + 8 + 18 * x, this.guiTop + 18 + y * 18);
                            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(this.container.inventorySlots.get(SlotHelper.PURE_DAISY_SLOTS.get(1).getIndex()).getHasStack() ? this.container.inventorySlots.get(SlotHelper.PURE_DAISY_SLOTS.get(1).getIndex()).getStack() : ItemStack.EMPTY, this.guiLeft + 116 + 18 * x, this.guiTop + 18 + y * 18);
                        }

                        i++;
                    }
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Time :", matrixStack);
                break;
            case BREWERY:
                minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModBlocks.brewery), this.guiLeft + 34, this.guiTop + 33);
                break;
            case PETAL_APOTHECARY:
                minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModBlocks.defaultAltar), this.guiLeft + Math.floorDiv(xSize, 2) + 21, this.guiTop + 4);
                break;
            case RUNIC_ALTAR:
                minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModBlocks.runeAltar), this.guiLeft + Math.floorDiv(xSize, 2) + 16, this.guiTop + 4);
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Mana :", matrixStack);
                break;
            case TERRA_PLATE:
                minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModItems.spark), this.guiLeft + 34, this.guiTop + 34);
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Mana :", matrixStack);
                break;
        }

        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    private void renderTextfieldTitle(String text, MatrixStack matrixStack)
    {
        RenderSystem.pushMatrix();
        double scale = 0.85D;
        RenderSystem.scaled(scale, scale, scale);
        Screen.drawString(matrixStack, font, new StringTextComponent(text), (int) (textField.x / scale), (int) ((textField.y - font.FONT_HEIGHT * scale) / scale), 0xFFFFFF);
        RenderSystem.popMatrix();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        this.textField.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(Character.isDigit(codePoint))
            this.textField.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.textField.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        this.minecraft.getTextureManager().bindTexture(getCurrentRecipe().getGuiTexture());

        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y)
    {
        drawCenteredString(matrixStack, minecraft.fontRenderer, References.getTranslate("screen.botania_recipe_creator." + RecipeFileUtils.getName(getCurrentRecipe().getRecipeType()).getPath() + ".title"), this.xSize / 2, -15, 0xFFFFFF);
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    private void setPos(Slot slot, int x, int y)
    {
        // Set x pos
        ReflectUtils.setField(slotXPosField, slot, x);

        // Set y pos
        ReflectUtils.setField(slotYPosField, slot, y);
    }

    private void nextPage()
    {
        this.currentScreenIndex = currentScreenIndex >= BotaniaRecipes.values().length - 1 ? 0 : currentScreenIndex + 1;
        updateServerIndex();
        updateScreen();
    }

    private void previousPage()
    {
        this.currentScreenIndex = currentScreenIndex <= 0 ? BotaniaRecipes.values().length - 1 : this.currentScreenIndex - 1;
        updateServerIndex();
        updateScreen();
    }

    private void updateServerIndex()
    {
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new SetBotaniaRecipeCreatorScreenIndexServerPacket(this.currentScreenIndex, this.container.tile.getPos()));
    }

    private void updateSlots()
    {
        this.container.inventorySlots.forEach(slot -> { if(slot instanceof SlotItemHandler) setPos(slot, -1000, -1000); });
        getCurrentRecipe().slots.forEach(ds ->setSlot(ds.getIndex(), ds.getxPos(), ds.getyPos()));
    }

    public enum BotaniaRecipes
    {
        MANA_INFUSION(ModRecipeTypes.MANA_INFUSION_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA, "mana_infusion_recipe_creator.png"), SlotHelper.MANA_INFUSION_SLOTS),
        ELVEN_TRADE(ModRecipeTypes.ELVEN_TRADE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA, "elven_trade_recipe_creator.png"), SlotHelper.ELVEN_TRADE_SLOTS),
        PURE_DAISY(ModRecipeTypes.PURE_DAISY_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA, "pure_daisy_recipe_creator.png"), SlotHelper.PURE_DAISY_SLOTS),
        BREWERY(ModRecipeTypes.BREW_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA, "brewery_recipe_creator.png"), SlotHelper.BREWERY_SLOTS),
        PETAL_APOTHECARY(ModRecipeTypes.PETAL_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA, "petal_apothecary_recipe_creator.png"), SlotHelper.PETAL_APOTHECARY_SLOTS),
        RUNIC_ALTAR(ModRecipeTypes.RUNE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA, "runic_altar_recipe_creator.png"), SlotHelper.RUNIC_ALTAR_SLOTS),
        TERRA_PLATE(ModRecipeTypes.TERRA_PLATE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA, "terra_plate_recipe_creator.png"), SlotHelper.TERRA_PLATE_SLOTS);

        private final IRecipeType<?> recipeType;
        private final ResourceLocation guiTexture;
        private final List<PositionnedSlot> slots;

        BotaniaRecipes(IRecipeType<?> recipeType, ResourceLocation guiTexture, List<PositionnedSlot> slots)
        {
            this.recipeType = recipeType;
            this.guiTexture = guiTexture;
            this.slots = slots;
        }

        public IRecipeType<?> getRecipeType()
        {
            return recipeType;
        }

        public ResourceLocation getGuiTexture()
        {
            return guiTexture;
        }

        public List<PositionnedSlot> getSlots()
        {
            return slots;
        }
    }
}