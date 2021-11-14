package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.systems.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.packets.*;
import fr.eno.craftcreator.screen.buttons.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.network.*;

import javax.annotation.*;

public class CraftingTableRecipeCreatorScreen extends TaggeableSlotsContainerScreen<CraftingTableRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;
    private static final ResourceLocation CRAFT_CREATOR_TABLE_GUI_TEXTURES = References.getLoc("textures/gui/container/crafting_table_recipe_creator.png");

    public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getPos());
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetCraftingTableRecipeCreatorTileInfosServerPacket(this.container.getTile().getPos(), this.container.windowId));

        this.addButton(craftTypeButton = new BooleanButton("craftType", guiLeft + 100, guiTop + 60, 68, 20, true, button -> InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new UpdateCraftingTableRecipeCreatorTilePacket(this.container.getTile().getPos(), this.isShaped()))));

        this.addButton(new ExecuteButton(guiLeft + 86, guiTop + 33, 30, button -> CraftHelper.createCraftingTableRecipe(this.container.getInventory(), this.getTaggedSlots(), this.isShaped())));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int yTextureOffset = ExecuteButton.isMouseHover(this.guiLeft + xSize - 20, guiTop, mouseX, mouseY, 20, 20) ? 20 : 0;
        minecraft.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/item_button.png"));
        Screen.blit(matrixStack, this.guiLeft + xSize - 20, guiTop, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
        minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(Items.CRAFTING_TABLE), this.guiLeft + xSize - 18, guiTop + 2);
    }

    private boolean isShaped()
    {
        return craftTypeButton.isOn();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CRAFT_CREATOR_TABLE_GUI_TEXTURES);
        int x = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, j, 0, 0, this.xSize, this.ySize);

        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }

    public void setShaped(boolean isShaped)
    {
        this.craftTypeButton.setOn(isShaped);
    }
}