package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.buttons.BooleanButton;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class CraftingTableRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<CraftingTableRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;

    public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getPos());
        isVanillaScreen = true;
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("shaped", this.getContainer().getTile().getPos(), InitPackets.PacketDataType.BOOLEAN));
        this.addButton(craftTypeButton = new BooleanButton("craftType", leftPos + 100, topPos + 60, 68, 20, true, (button) ->
        {
        }));

        updateScreen();
    }

    @Override
    protected List<ModRecipeCreator> getAvailableRecipesCreator()
    {
        return Collections.singletonList(ModRecipeCreator.CRAFTING_TABLE);
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);
        if(dataName.equals("shaped")) this.setShaped((boolean) data);
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        setExecuteButtonPos(leftPos + 86, topPos + 33);
        executeButton.setWidth(30);
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        super.getRecipeInfos();
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean(RecipeInfos.Parameters.SHAPED, this.craftTypeButton.isOn()));
        return this.recipeInfos;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        craftTypeButton.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    private boolean isShaped()
    {
        return craftTypeButton.isOn();
    }

    @Override
    protected List<PositionnedSlot> getTaggeableSlots()
    {
        return SlotHelper.CRAFTING_TABLE_SLOTS;
    }

    @Override
    protected PairValues<Integer, Integer> getIconPos()
    {
        return PairValues.create(this.leftPos + imageWidth - 18, topPos + 2);
    }

    @Override
    protected Item getRecipeIcon()
    {
        return Items.CRAFTING_TABLE;
    }

    public void setShaped(boolean isShaped)
    {
        this.craftTypeButton.setOn(isShaped);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("shaped", this.getContainer().getTile().getPos(), InitPackets.PacketDataType.BOOLEAN, isShaped()));
    }
}