package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.systems.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.screen.buttons.*;
import fr.eno.craftcreator.tileentity.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.items.*;

import java.awt.*;
import java.util.*;

public class CraftingTableRecipeCreatorScreen extends ContainerScreen<CraftingTableRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;
    private final Map<Slot, ResourceLocation> taggedSlots;
    private Slot selectedSlot;
    private GuiList<ResourceLocation> guiTagList;
    private static final ResourceLocation CRAFT_CREATOR_TABLE_GUI_TEXTURES = new ResourceLocation(References.MOD_ID, "textures/gui/container/crafting_table_recipe_creator.png");

    public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.taggedSlots = new HashMap<>();
    }

    @Override
    protected void init()
    {
        super.init();
        this.addButton(craftTypeButton = new BooleanButton("craftType", guiLeft + 100, guiTop + 60, 68, 20, true));

        this.addButton(new ExecuteButton(guiLeft + 86, guiTop + 33, 30, button -> CraftHelper.createCraftingTableRecipe(this.container.getInventory(), this.getTaggedSlots(), this.isShaped())));

        this.selectedSlot = null;
        this.guiTagList = new GuiList<>(this, this.guiLeft - 120, this.guiTop, 120, 18);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseClick)
    {
        Slot slot = this.getSelectedSlot(mouseX, mouseY);

        if((slot instanceof SlotItemHandler) && slot != null && !slot.getStack().getItem().getTags().isEmpty())
        {
            this.guiTagList.setKeys(null);
            this.selectedSlot = null;

            boolean checkInventory = slot != null && ((SlotItemHandler) slot).getItemHandler() instanceof CraftingTableRecipeCreatorTile;

            if(checkInventory && Screen.hasControlDown())
            {
                this.selectedSlot = slot;
                this.guiTagList.setKeys(new ArrayList<>(slot.getStack().getItem().getTags()));

                if(this.taggedSlots.containsKey(slot))
                    this.guiTagList.setSelectedKey(this.taggedSlots.get(slot));

                return true;
            }
        }

        this.guiTagList.mouseClicked((int) mouseX, (int) mouseY, resourceLocation -> this.taggedSlots.put(this.selectedSlot, resourceLocation));

        return super.mouseClicked(mouseX, mouseY, mouseClick);
    }

    private Slot getSelectedSlot(double mouseX, double mouseY)
    {
        for(int i = 0; i < this.container.inventorySlots.size(); ++i)
        {
            Slot slot = this.container.inventorySlots.get(i);
            if(this.isSlotSelected(slot, mouseX, mouseY) && slot.isEnabled())
            {
                return slot;
            }
        }

        return null;
    }

    private boolean isSlotSelected(Slot slotIn, double mouseX, double mouseY)
    {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);
        minecraft.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/item_button.png"));
        int yTextureOffset = GuiUtils.isMouseHover(this.guiLeft + xSize - 20, guiTop, mouseX, mouseY, 20, 20) ? 20 : 0;
        Screen.blit(this.guiLeft + xSize - 20, guiTop, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
        minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(Items.CRAFTING_TABLE), this.guiLeft + xSize - 18, guiTop + 2);
        if(Screen.hasShiftDown()) this.drawString(this.font, References.getTranslate("screen.crafting.info").getFormattedText(), this.guiLeft, this.guiTop - 8, Color.GRAY.getRGB());

        if(this.guiTagList.getKeys() != null)
            this.guiTagList.render(mouseX, mouseY);
    }

    private boolean isShaped()
    {
        return craftTypeButton.isOn();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        this.renderBackground();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CRAFT_CREATOR_TABLE_GUI_TEXTURES);
        int x = this.guiLeft;
        int y = this.guiTop;
        int j = (this.height - this.ySize) / 2;
        this.blit(x, j, 0, 0, this.xSize, this.ySize);

        for(Slot slot : this.taggedSlots.keySet())
        {
            fill(x + slot.xPos, y + slot.yPos, x + slot.xPos + 16, y + slot.yPos + 16, new Color(0F, 0.5F, 0F, 0.5F).getRGB());
        }

        if(this.selectedSlot != null)
            fill(x + selectedSlot.xPos, y + selectedSlot.yPos, x + selectedSlot.xPos + 16, y + selectedSlot.yPos + 16, Color.YELLOW.getRGB());
    }

    public Map<Slot, ResourceLocation> getTaggedSlots()
    {
        return this.taggedSlots;
    }
}