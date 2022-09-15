package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.packets.GetCraftingTableRecipeCreatorTileInfosServerPacket;
import fr.eno.craftcreator.packets.UpdateCraftingTableRecipeCreatorTilePacket;
import fr.eno.craftcreator.screen.buttons.BooleanButton;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;

public class CraftingTableRecipeCreatorScreen extends TaggeableSlotsContainerScreen<CraftingTableRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;
    private static final ResourceLocation CRAFT_CREATOR_TABLE_GUI_TEXTURES = References.getLoc("textures/gui/container/crafting_table_recipe_creator.png");
    private SimpleCheckBox isKubeJSRecipeButton;

    public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetCraftingTableRecipeCreatorTileInfosServerPacket(this.getMenu().getTile().getBlockPos(), this.getMenu().containerId));


        this.addRenderableWidget(isKubeJSRecipeButton = new SimpleCheckBox(5, this.height - 20, 15, 15, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));
        this.addRenderableWidget(craftTypeButton = new BooleanButton("craftType", leftPos + 100, topPos + 60, 68, 20, true, button -> InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new UpdateCraftingTableRecipeCreatorTilePacket(this.getMenu().getTile().getBlockPos(), this.isShaped()))));
        this.addRenderableWidget(new ExecuteButton(leftPos + 86, topPos + 33, 30, button -> MinecraftRecipeSerializer.createCraftingTableRecipe(this.getMenu().getItems(), this.getTaggedSlots(), this.isShaped(), isKubeJSRecipeButton.selected())));

        if(!SupportedMods.isKubeJSLoaded())
            this.isKubeJSRecipeButton.visible = false;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int yTextureOffset = ExecuteButton.isMouseHover(this.leftPos + imageWidth - 20, topPos, mouseX, mouseY, 20, 20) ? 20 : 0;
        minecraft.getTextureManager().bindForSetup(References.getLoc("textures/gui/buttons/item_button.png"));
        Screen.blit(matrixStack, this.leftPos + imageWidth - 20, topPos, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
        minecraft.getItemRenderer().renderGuiItem(new ItemStack(Items.CRAFTING_TABLE), this.leftPos + imageWidth - 18, topPos + 2);
    }

    private boolean isShaped()
    {
        return craftTypeButton.isOn();
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        this.renderBackground(matrixStack);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindForSetup(CRAFT_CREATOR_TABLE_GUI_TEXTURES);
        int x = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, x, j, 0, 0, this.imageWidth, this.imageHeight);

        super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
    }

    public void setShaped(boolean isShaped)
    {
        this.craftTypeButton.setOn(isShaped);
    }
}