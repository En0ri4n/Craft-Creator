package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.screen.buttons.BooleanButton;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.List;

public class CraftingTableRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<CraftingTableRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;
    private SimpleCheckBox isKubeJSRecipeButton;

    public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("shaped", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN));

        this.addRenderableWidget(isKubeJSRecipeButton = new SimpleCheckBox(5, this.height - 20, 15, 15, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));
        this.addRenderableWidget(craftTypeButton = new BooleanButton("craftType", leftPos + 100, topPos + 60, 68, 20, true, (button) -> {}));

        updateScreen();
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);
        if(dataName.equals("shaped"))
            this.setShaped((boolean) data);
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        if(!SupportedMods.isKubeJSLoaded()) this.isKubeJSRecipeButton.visible = false;
        executeButton.setWidth(30);
        setExecuteButtonPos(leftPos + 86, topPos + 33);

    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        super.getRecipeInfos();
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean("shaped", this.craftTypeButton.isOn()));
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean("kubejs_recipe", this.isKubeJSRecipeButton.selected()));
        return this.recipeInfos;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int yTextureOffset = ExecuteButton.isMouseHover(this.leftPos + imageWidth - 20, topPos, mouseX, mouseY, 20, 20) ? 20 : 0;
        RenderSystem.setShaderTexture(0, References.getLoc("textures/gui/buttons/item_button.png"));
        RenderSystem.enableBlend();
        Screen.blit(matrixStack, this.leftPos + imageWidth - 20, topPos, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);

        renderTooltip(matrixStack, mouseX, mouseY);
    }

    private boolean isShaped()
    {
        return craftTypeButton.isOn();
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
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
    protected void updateSlots()
    {
        this.getMenu().activeSlots(true);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("shaped", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN, isShaped()));
    }
}