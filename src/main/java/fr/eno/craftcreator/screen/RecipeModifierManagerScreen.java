package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.container.RecipeModifierContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.screen.container.base.TaggeableSlotsContainerScreen;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeModifierManagerScreen extends TaggeableSlotsContainerScreen<RecipeModifierContainer> implements INamedContainerProvider
{
    private static final ResourceLocation GUI_TEXTURE = References.getLoc("textures/gui/container/recipe_modifier.png");
    private final List<SimpleListWidget> lists;
    private final Screen parent;

    public RecipeModifierManagerScreen(Screen parent)
    {
        super(RecipeModifierContainer.create(), ClientUtils.getClientPlayer().inventory, new StringTextComponent("Recipe Modifier Manager"));
        this.leftPos = (this.width - (this.width / 4 + 5)) / 2 + (this.width / 4 + 5) - 256 / 2;
        this.topPos = 5 + (this.height - 20 - 5) / 2 - 256 / 2;
        this.lists = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        super.init();

        this.lists.clear();

        int bottomHeight = 20;

        this.lists.add(new SimpleListWidget(ClientUtils.getMinecraft(), 5, 5, this.width / 4, this.height - bottomHeight, 15, 15, 5, References.getTranslate("screen.recipe_manager.list.modified_recipes"), null, false));

        //this.lists.get(0).setEntries(ListEntriesHelper.getModifiedRecipesEntryList());

        this.addButton(new SimpleButton(References.getTranslate("screen.recipe_modifier.button.back"), this.width / 2 - 40, this.height - bottomHeight - 7, 80, 20, button -> ClientUtils.openScreen(this.parent)));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.lists.get(0).render(matrixStack, mouseX, mouseY, partialTicks);
        String msg = "NOT IMPLEMENTED YET";
        Screen.drawString(matrixStack, this.font, msg, this.width / 2 - this.font.width(msg) / 2, this.height / 2 - this.font.lineHeight / 2, 0xFFFF0000);
        ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(Items.ACACIA_BOAT), 100, 100);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double p_97812_, double p_97813_, int p_97814_)
    {
        return super.mouseReleased(p_97812_, p_97813_, p_97814_);
    }

    @Override
    public boolean mouseDragged(double p_97752_, double p_97753_, int p_97754_, double p_97755_, double p_97756_)
    {
        return super.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
    }

    @Override
    public boolean keyPressed(int p_97765_, int p_97766_, int p_97767_)
    {
        return super.keyPressed(p_97765_, p_97766_, p_97767_);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int pX, int pY)
    {
        ClientUtils.bindTexture(GUI_TEXTURE);
        int size = 256;
        int x = (this.width - (this.width / 4 + 5)) / 2 + (this.width / 4 + 5) - size / 2;
        int y = 5 + (this.height - 20 - 5) / 2 - size / 2;
        blit(matrixStack, x, y, size, size, 0, 0, size, size, size, size);

        super.renderBackground(matrixStack);
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return Collections.emptyList();
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return new ArrayList<>();
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return References.getTranslate("screen.recipe_modifier.title");
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory inventory, PlayerEntity player)
    {
        return RecipeModifierContainer.create();
    }
}
