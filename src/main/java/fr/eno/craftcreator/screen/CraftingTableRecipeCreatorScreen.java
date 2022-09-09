package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.packets.GetCraftingTableRecipeCreatorTileInfosServerPacket;
import fr.eno.craftcreator.packets.UpdateCraftingTableRecipeCreatorTilePacket;
import fr.eno.craftcreator.screen.buttons.BooleanButton;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;

public class CraftingTableRecipeCreatorScreen extends TaggeableSlotsContainerScreen<CraftingTableRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;
    private static final ResourceLocation CRAFT_CREATOR_TABLE_GUI_TEXTURES = References.getLoc("textures/gui/container/crafting_table_recipe_creator.png");
    private SimpleCheckBox isKubeJSRecipeButton;

    public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getPos());
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetCraftingTableRecipeCreatorTileInfosServerPacket(this.container.getTile().getPos(), this.container.windowId));


        this.addButton(isKubeJSRecipeButton = new SimpleCheckBox(5, this.height - 20, 15, 15, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));
        this.addButton(craftTypeButton = new BooleanButton("craftType", guiLeft + 100, guiTop + 60, 68, 20, true, button -> InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new UpdateCraftingTableRecipeCreatorTilePacket(this.container.getTile().getPos(), this.isShaped()))));
        this.addButton(new ExecuteButton(guiLeft + 86, guiTop + 33, 30, button -> MinecraftRecipeSerializer.createCraftingTableRecipe(this.container.getInventory(), this.getTaggedSlots(), this.isShaped(), isKubeJSRecipeButton.isChecked())));
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