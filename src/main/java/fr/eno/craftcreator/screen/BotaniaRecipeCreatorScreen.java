package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.kubejs.managers.BotaniaRecipeManagers;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.packets.GetBotaniaRecipeCreatorScreenIndexPacket;
import fr.eno.craftcreator.packets.SetBotaniaRecipeCreatorScreenIndexServerPacket;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.ReflectUtils;
import fr.eno.craftcreator.utils.SlotHelper;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class BotaniaRecipeCreatorScreen extends TaggeableSlotsContainerScreen<BotaniaRecipeCreatorContainer>
{
    private EditBox textField;

    private ExecuteButton executeButton;

    private int currentScreenIndex;

    public BotaniaRecipeCreatorScreen(BotaniaRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.tile.getBlockPos());
        this.currentScreenIndex = 0;
    }

    @Override
    protected void init()
    {
        super.init();

        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetBotaniaRecipeCreatorScreenIndexPacket(this.getMenu().tile.getBlockPos()));

        this.addRenderableWidget(executeButton = new ExecuteButton(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35,40, (button) -> BotaniaRecipeManagers.createRecipe(getCurrentRecipe(), this.getMenu().slots.stream().filter(slot -> slot instanceof SlotItemHandler).collect(Collectors.toList()), getTagged(), getParameterValue())));

        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.botania_recipe_creator.button.next"), this.leftPos + this.imageWidth + 3, this.topPos + this.imageHeight - 66, 10, 20, (button) -> nextPage()));
        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.botania_recipe_creator.button.previous"),  this.leftPos - 3 - 10, this.topPos + this.imageHeight - 66,10, 20, (button) -> previousPage()));

        textField = new EditBox(minecraft.font, leftPos + imageWidth - 44, topPos + imageHeight / 2 - 16, 35, 10, new TextComponent(""));
        textField.setValue(String.valueOf(100));

        updateScreen();
    }

    private int getParameterValue()
    {
        try
        {
            return Integer.parseInt(this.textField.getValue());
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
                setTextFieldPos(leftPos + imageWidth - 44, textField.y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
                break;
            case ELVEN_TRADE:
            case PURE_DAISY:
                setTextFieldPos(leftPos + imageWidth / 2 - 35 / 2, textField.y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
                break;
            case BREWERY:
            case TERRA_PLATE:
                setTextFieldPos(leftPos + imageWidth / 2, textField.y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 5, this.topPos + 31);
                break;
            case PETAL_APOTHECARY:
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 1, this.topPos + 31);
                break;
            case RUNIC_ALTAR:
                setTextFieldPos(leftPos + imageWidth / 2, textField.y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 2, this.topPos + 31);
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
        setPos(this.getMenu().slots.stream().filter(s -> s.getSlotIndex() == index).findFirst().orElse(null), x, y);
    }

    private BotaniaRecipes getCurrentRecipe()
    {
        return BotaniaRecipes.values()[currentScreenIndex];
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION:
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.creativePool), this.leftPos + imageWidth / 2 - 8, this.topPos + 14);
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Mana :", matrixStack);
                break;
            case ELVEN_TRADE:
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.livingwoodGlimmering), this.leftPos + imageWidth / 2 - 8, this.topPos + 14);
                break;
            case PURE_DAISY:
                int i = 0;
                for(int x = 0; x < 3; x++)
                    for(int y = 0; y < 3; y++)
                    {
                        if(i == 4)
                        {
                            minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                            minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                        }
                        else if(i != 5)
                        {
                            minecraft.getItemRenderer().renderAndDecorateFakeItem(this.getMenu().slots.get(SlotHelper.PURE_DAISY_SLOTS.get(0).getIndex()).hasItem() ? this.getMenu().slots.get(SlotHelper.PURE_DAISY_SLOTS.get(0).getIndex()).getItem() : ItemStack.EMPTY, this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                            minecraft.getItemRenderer().renderAndDecorateFakeItem(this.getMenu().slots.get(SlotHelper.PURE_DAISY_SLOTS.get(1).getIndex()).hasItem() ? this.getMenu().slots.get(SlotHelper.PURE_DAISY_SLOTS.get(1).getIndex()).getItem() : ItemStack.EMPTY, this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                        }

                        i++;
                    }
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Time :", matrixStack);
                break;
            case BREWERY:
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.brewery), this.leftPos + 34, this.topPos + 33);
                break;
            case PETAL_APOTHECARY:
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.defaultAltar), this.leftPos + Math.floorDiv(imageWidth, 2) + 21, this.topPos + 4);
                break;
            case RUNIC_ALTAR:
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.runeAltar), this.leftPos + Math.floorDiv(imageWidth, 2) + 16, this.topPos + 4);
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Mana :", matrixStack);
                break;
            case TERRA_PLATE:
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModItems.spark), this.leftPos + 34, this.topPos + 34);
                this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle("Mana :", matrixStack);
                break;
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    private void renderTextfieldTitle(String text, PoseStack matrixStack)
    {
        matrixStack.pushPose();
        float scale = 0.85F;
        matrixStack.scale(scale, scale, scale);
        Screen.drawString(matrixStack, font, new TextComponent(text), (int) (textField.x / scale), (int) ((textField.y - font.lineHeight * scale) / scale), 0xFFFFFF);
        matrixStack.popPose();
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
        drawCenteredString(matrixStack, minecraft.font, References.getTranslate("screen.botania_recipe_creator." + RecipeFileUtils.getName(getCurrentRecipe().getRecipeType()).getPath() + ".title"), this.imageWidth / 2, -15, 0xFFFFFF);
        super.renderLabels(matrixStack, pMouseX, pMouseY);
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
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new SetBotaniaRecipeCreatorScreenIndexServerPacket(this.currentScreenIndex, this.getMenu().tile.getBlockPos()));
    }

    private void updateSlots()
    {
        this.getMenu().slots.forEach(slot -> { if(slot instanceof SlotItemHandler) setPos(slot, -1000, -1000); });
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

        private final RecipeType<?> recipeType;
        private final ResourceLocation guiTexture;
        private final List<PositionnedSlot> slots;

        BotaniaRecipes(RecipeType<?> recipeType, ResourceLocation guiTexture, List<PositionnedSlot> slots)
        {
            this.recipeType = recipeType;
            this.guiTexture = guiTexture;
            this.slots = slots;
        }

        public RecipeType<?> getRecipeType()
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