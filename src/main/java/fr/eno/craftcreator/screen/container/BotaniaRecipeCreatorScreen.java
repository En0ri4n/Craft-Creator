package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class BotaniaRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<BotaniaRecipeCreatorContainer>
{
    private static final int MANA_FIELD = 0;

    public BotaniaRecipeCreatorScreen(BotaniaRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
    }

    @Override
    protected void init()
    {
        super.init();

        textFields.set(MANA_FIELD, new EditBox(minecraft.font, leftPos + imageWidth - 44, topPos + imageHeight / 2 - 16, 35, 10, new TextComponent("")));
        textFields.get(MANA_FIELD).setValue(String.valueOf(100));
    }

    @Override
    protected void updateScreen()
    {
        this.updateSlots();

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION ->
            {
                setTextFieldPos(MANA_FIELD, leftPos + imageWidth - 44, textFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
            }
            case ELVEN_TRADE, PURE_DAISY ->
            {
                setTextFieldPos(MANA_FIELD, leftPos + imageWidth / 2 - 35 / 2, textFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
            }
            case BREWERY, TERRA_PLATE ->
            {
                setTextFieldPos(MANA_FIELD, leftPos + imageWidth / 2, textFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 5, this.topPos + 31);
            }
            case PETAL_APOTHECARY -> setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 1, this.topPos + 31);
            case RUNIC_ALTAR ->
            {
                setTextFieldPos(MANA_FIELD, leftPos + imageWidth / 2, textFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 2, this.topPos + 31);
            }
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        assert minecraft != null;
        this.renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION ->
            {
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.creativePool), this.leftPos + imageWidth / 2 - 8, this.topPos + 14);
                renderTextfieldTitle(MANA_FIELD, "Mana :", matrixStack);
            }
            case ELVEN_TRADE ->
                    minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.livingwoodGlimmering), this.leftPos + imageWidth / 2 - 8, this.topPos + 14);
            case PURE_DAISY ->
            {
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
                this.textFields.get(MANA_FIELD).render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle(MANA_FIELD, "Time :", matrixStack);
            }
            case BREWERY ->
            {
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.brewery), this.leftPos + 34, this.topPos + 33);
                this.textFields.get(MANA_FIELD).visible = false;
            }
            case PETAL_APOTHECARY ->
            {
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.defaultAltar), this.leftPos + Math.floorDiv(imageWidth, 2) + 21, this.topPos + 4);
                this.textFields.get(MANA_FIELD).visible = false;
            }
            case RUNIC_ALTAR ->
            {
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModBlocks.runeAltar), this.leftPos + Math.floorDiv(imageWidth, 2) + 16, this.topPos + 4);
                this.textFields.get(MANA_FIELD).render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle(MANA_FIELD, "Mana :", matrixStack);
            }
            case TERRA_PLATE ->
            {
                minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModItems.spark), this.leftPos + 34, this.topPos + 34);
                this.textFields.get(MANA_FIELD).render(matrixStack, mouseX, mouseY, partialTicks);
                renderTextfieldTitle(MANA_FIELD, "Mana :", matrixStack);
            }
        }
    }
}