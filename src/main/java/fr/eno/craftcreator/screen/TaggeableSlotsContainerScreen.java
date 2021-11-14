package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.packets.*;
import fr.eno.craftcreator.tileentity.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.network.*;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;

public abstract class TaggeableSlotsContainerScreen<T extends Container> extends ContainerScreen<T>
{
    private Map<SlotItemHandler, ResourceLocation> taggedSlots;
    private GuiList<ResourceLocation> guiTagList;
    private SlotItemHandler selectedSlot;
    private final BlockPos pos;

    public TaggeableSlotsContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, BlockPos pos)
    {
        super(screenContainer, inv, titleIn);
        this.taggedSlots = new HashMap<>();
        this.pos = pos;
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetTaggeableContainerRecipeCreatorTileInfosServerPacket(this.pos, this.container.windowId));

        this.guiTagList = new GuiList<>(this.guiLeft, this.guiTop + 1, 18);
        this.selectedSlot = null;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if(this.guiTagList.getKeys() != null)
            this.guiTagList.render(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y)
    {
        if(Screen.hasShiftDown() || Screen.hasControlDown()) drawString(matrixStack, this.font, References.getTranslate("screen.crafting.info").getString(), 0, this.ySize, java.awt.Color.GRAY.getRGB());
    }

    private void updateServerTileData()
    {
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new UpdateTaggeableContainerRecipeCreatorTilePacket(this.pos, getTagged(this.taggedSlots)));
    }

    private Map<Integer, ResourceLocation> getTagged(Map<SlotItemHandler, ResourceLocation> taggedSlots)
    {
        Map<Integer, ResourceLocation> tagged = new HashMap<>();

        for(SlotItemHandler slot1 : this.taggedSlots.keySet())
            tagged.put(slot1.getSlotIndex(), taggedSlots.get(slot1));

        return tagged;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        Slot slot = this.getSelectedSlot(mouseX, mouseY);

        if((slot instanceof SlotItemHandler) && slot != null && slot.getSlotIndex() != 9)
        {
            boolean checkInventory = slot != null && ((SlotItemHandler) slot).getItemHandler() instanceof TaggeableInventoryContainerTileEntity;

            if(checkInventory && !Screen.hasControlDown())
            {
                this.guiTagList.setKeys(null);
                this.selectedSlot = null;
                this.taggedSlots.remove(slot);
                updateServerTileData();
                return super.mouseClicked(mouseX, mouseY, button);
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

        if(mouseX <= guiLeft || mouseX >= guiLeft + xSize || mouseY <= guiTop || mouseY >= guiTop + ySize)
        {
            this.guiTagList.setKeys(null);
            this.guiTagList.setSelectedKey(null);
            this.selectedSlot = null;
            return super.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
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

    public void setTaggedSlots(Map<Integer, ResourceLocation> taggedSlots)
    {
        Map<SlotItemHandler, ResourceLocation> taggedSlots1 = new HashMap<>();

        for(Integer integer : taggedSlots.keySet())
        {
            taggedSlots1.put((SlotItemHandler) this.container.getSlot(integer), taggedSlots.get(integer));
        }

        this.taggedSlots = taggedSlots1;
    }

    public Map<SlotItemHandler, ResourceLocation> getTaggedSlots()
    {
        return this.taggedSlots;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        int x = this.guiLeft;
        int y = this.guiTop;
        
        for(Slot slot : this.taggedSlots.keySet())
        {
            fill(matrixStack, x + slot.xPos, y  + slot.yPos, x + slot.xPos + 16, y  + slot.yPos + 16, new java.awt.Color(0F, 0.5F, 0F, 0.5F).getRGB());
        }

        if(this.selectedSlot != null)
            fill(matrixStack, x + selectedSlot.xPos, y  + selectedSlot.yPos, x + selectedSlot.xPos + 16, y  + selectedSlot.yPos + 16, java.awt.Color.YELLOW.getRGB());
    }

    @Override
    public void onClose()
    {
        updateServerTileData();
        super.onClose();
    }
}
