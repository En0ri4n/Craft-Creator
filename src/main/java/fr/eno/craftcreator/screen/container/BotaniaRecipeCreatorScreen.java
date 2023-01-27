package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.BotaniaRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.screen.widgets.NumberDataFieldWidget;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;

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

        addNumberField(leftPos + imageWidth - 44, topPos + imageHeight / 2 - 16, 35, 10, 100, 1);

        updateScreen();
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        for(NumberDataFieldWidget textField : dataFields)
        {
            if(textField.visible)
            {
                if(textField.getValue().isEmpty())
                    textField.setNumberValue(100);

                if(this.getCurrentRecipe().equals(ModRecipeCreator.PURE_DAISY))
                {
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("time", textField.getIntValue()));
                    continue;
                }

                this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("mana", textField.getIntValue()));
            }
        }

        return this.recipeInfos;
    }

    @Override
    protected void updateScreen()
    {
        this.updateSlots();

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION ->
            {
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth - 44, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
            }
            case ELVEN_TRADE, PURE_DAISY ->
            {
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2 - 35 / 2, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
            }
            case BREWERY, TERRA_PLATE ->
            {
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 5, this.topPos + 31);
            }
            case PETAL_APOTHECARY -> setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 1, this.topPos + 31);
            case RUNIC_ALTAR ->
            {
                showDataField(0);
                setDataFieldPos(MANA_FIELD, leftPos + imageWidth / 2, dataFields.get(MANA_FIELD).y);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 2, this.topPos + 31);
            }
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case MANA_INFUSION, ELVEN_TRADE, RUNIC_ALTAR, TERRA_PLATE, BREWERY, PETAL_APOTHECARY ->
                    renderDataFieldAndTitle(MANA_FIELD, References.getTranslate("screen.botania_recipe_creator.field.mana"), matrixStack, mouseX, mouseY, partialTicks);
            case PURE_DAISY ->
            {
                int i = 0;
                for(int x = 0; x < 3; x++)
                    for(int y = 0; y < 3; y++)
                    {
                        if(i == 4)
                        {
                            getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                            getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ModSubtiles.pureDaisy), this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                        }
                        else if(i != 5)
                        {
                            Slot inputSlot = PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, this.getMenu().slots).get(0);
                            Slot outputSlot = PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, this.getMenu().slots).get(1);
                            getItemRenderer().renderAndDecorateFakeItem(inputSlot.hasItem() ? inputSlot.getItem() : ItemStack.EMPTY, this.leftPos + 8 + 18 * x, this.topPos + 18 + y * 18);
                            getItemRenderer().renderAndDecorateFakeItem(outputSlot.hasItem() ? outputSlot.getItem() : ItemStack.EMPTY, this.leftPos + 116 + 18 * x, this.topPos + 18 + y * 18);
                        }

                        i++;
                    }

                renderDataFieldAndTitle(MANA_FIELD, References.getTranslate("screen.botania_recipe_creator.field.time"), matrixStack, mouseX, mouseY, partialTicks);
            }
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected List<PositionnedSlot> getTaggeableSlots()
    {
        return SlotHelper.BOTANIA_SLOTS;
    }

    @Override
    protected Item getRecipeIcon()
    {
        return switch(getCurrentRecipe())
        {
            case MANA_INFUSION -> ModBlocks.creativePool.asItem();
            case ELVEN_TRADE -> ModBlocks.livingwoodGlimmering.asItem();
            case BREWERY -> ModBlocks.brewery.asItem();
            case PETAL_APOTHECARY -> ModBlocks.defaultAltar.asItem();
            case RUNIC_ALTAR -> ModBlocks.runeAltar.asItem();
            case TERRA_PLATE -> ModItems.spark.asItem();
            default -> Items.AIR;
        };
    }

    @Override
    protected PairValues<Integer, Integer> getIconPos()
    {
        return switch(getCurrentRecipe())
        {
            case MANA_INFUSION, ELVEN_TRADE -> PairValues.create(this.leftPos + imageWidth / 2 - 8, this.topPos + 14);
            case BREWERY -> PairValues.create(this.leftPos + 34, this.topPos + 33);
            case PETAL_APOTHECARY -> PairValues.create(this.leftPos + Math.floorDiv(imageWidth, 2) + 21, this.topPos + 4);
            case RUNIC_ALTAR -> PairValues.create(this.leftPos + Math.floorDiv(imageWidth, 2) + 16, this.topPos + 4);
            case TERRA_PLATE -> PairValues.create(this.leftPos + 34, this.topPos + 34);
            default -> super.getIconPos();
        };
    }
}