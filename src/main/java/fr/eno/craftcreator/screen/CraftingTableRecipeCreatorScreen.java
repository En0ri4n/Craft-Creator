package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.systems.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.packets.*;
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
import net.minecraftforge.fml.network.*;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.awt.Color;
import java.util.*;

public class CraftingTableRecipeCreatorScreen extends ContainerScreen<CraftingTableRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;
    private Map<SlotItemHandler, ResourceLocation> taggedSlots;
    private SlotItemHandler selectedSlot;
    private GuiList<ResourceLocation> guiTagList;
    private static final ResourceLocation CRAFT_CREATOR_TABLE_GUI_TEXTURES = References.getLoc("textures/gui/container/crafting_table_recipe_creator.png");

    public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.taggedSlots = new HashMap<>();
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetCraftingTableRecipeCreatorTileInfosServerPacket(this.container.getTile().getPos(), this.container.windowId));

        this.addButton(craftTypeButton = new BooleanButton("craftType", guiLeft + 100, guiTop + 60, 68, 20, true, button -> updateServerTileData()));

        this.addButton(new ExecuteButton(guiLeft + 86, guiTop + 33, 30, button -> CraftHelper.createCraftingTableRecipe(this.container.getInventory(), this.getTaggedSlots(), this.isShaped())));

        this.selectedSlot = null;
        this.guiTagList = new GuiList<>(this.guiLeft, this.guiTop + 1, 18);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseClick)
    {
        Slot slot = this.getSelectedSlot(mouseX, mouseY);

        if((slot instanceof SlotItemHandler) && slot != null && slot.getSlotIndex() != 9)
        {
            boolean checkInventory = slot != null && ((SlotItemHandler) slot).getItemHandler() instanceof CraftingTableRecipeCreatorTile;

            if(checkInventory && !Screen.hasControlDown())
            {
                this.guiTagList.setKeys(null);
                this.selectedSlot = null;
                this.taggedSlots.remove(slot);
                updateServerTileData();
                return super.mouseClicked(mouseX, mouseY, mouseClick);
            }

            this.guiTagList.setKeys(null);
            this.guiTagList.setSelectedKey(null);
            this.selectedSlot = null;

            if(checkInventory && Screen.hasControlDown() && !slot.getStack().getItem().getTags().isEmpty())
            {
                this.selectedSlot = (SlotItemHandler) slot;
                this.guiTagList.setKeys(new ArrayList<>(slot.getStack().getItem().getTags()));

                if(this.taggedSlots.containsKey(this.selectedSlot))
                    this.guiTagList.setSelectedKey(this.taggedSlots.get(this.selectedSlot));

                updateServerTileData();
                return true;
            }
            else if(checkInventory && Screen.hasControlDown() && slot.getStack().getItem().getTags().isEmpty())
            {
                this.guiTagList.setKeys(null);
                this.guiTagList.setSelectedKey(null);
                this.selectedSlot = null;
                updateServerTileData();
                return true;
            }
        }

        this.guiTagList.mouseClicked((int) mouseX, (int) mouseY, resourceLocation ->
        {
            if(resourceLocation == null)
            {
                this.taggedSlots.remove(this.selectedSlot);
                updateServerTileData();
                return;
            }

            this.taggedSlots.put(this.selectedSlot, resourceLocation);
            updateServerTileData();
        });

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
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int yTextureOffset = ExecuteButton.isMouseHover(this.guiLeft + xSize - 20, guiTop, mouseX, mouseY, 20, 20) ? 20 : 0;
        minecraft.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/item_button.png"));
        Screen.blit(matrixStack, this.guiLeft + xSize - 20, guiTop, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
        minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(Items.CRAFTING_TABLE), this.guiLeft + xSize - 18, guiTop + 2);

        if(Screen.hasShiftDown() || Screen.hasControlDown()) drawString(matrixStack, this.font, References.getTranslate("screen.crafting.info").getString(), this.guiLeft, this.guiTop - 8, Color.GRAY.getRGB());

        if(this.guiTagList.getKeys() != null)
            this.guiTagList.render(matrixStack, mouseX, mouseY);
    }

    private boolean isShaped()
    {
        return craftTypeButton.isOn();
    }

    private void updateServerTileData()
    {
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new UpdateCraftingTableRecipeCreatorTilePacket(this.container.getTile().getPos(), this.isShaped(), getTagged(this.taggedSlots)));
    }
    private Map<Integer, ResourceLocation> getTagged(Map<SlotItemHandler, ResourceLocation> taggedSlots)
    {
        Map<Integer, ResourceLocation> tagged = new HashMap<>();

        for(SlotItemHandler slot1 : this.taggedSlots.keySet())
            tagged.put(slot1.getSlotIndex(), taggedSlots.get(slot1));

        return tagged;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CRAFT_CREATOR_TABLE_GUI_TEXTURES);
        int x = this.guiLeft;
        int y = this.guiTop;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, j, 0, 0, this.xSize, this.ySize);

        for(Slot slot : this.taggedSlots.keySet())
        {
            fill(matrixStack, x + slot.xPos, y + slot.yPos, x + slot.xPos + 16, y + slot.yPos + 16, new Color(0F, 0.5F, 0F, 0.5F).getRGB());
        }

        if(this.selectedSlot != null)
            fill(matrixStack, x + selectedSlot.xPos, y + selectedSlot.yPos, x + selectedSlot.xPos + 16, y + selectedSlot.yPos + 16, Color.YELLOW.getRGB());
    }

    public Map<SlotItemHandler, ResourceLocation> getTaggedSlots()
    {
        return this.taggedSlots;
    }

    public void setInfos(boolean isShaped, Map<Integer, ResourceLocation> taggedSlots)
    {
        Map<SlotItemHandler, ResourceLocation> taggedSlots1 = new HashMap<>();

        for(Integer integer : taggedSlots.keySet())
        {
            taggedSlots1.put((SlotItemHandler) this.container.getSlot(integer), taggedSlots.get(integer));
        }

        this.taggedSlots = taggedSlots1;

        this.craftTypeButton.setOn(isShaped);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {}
}