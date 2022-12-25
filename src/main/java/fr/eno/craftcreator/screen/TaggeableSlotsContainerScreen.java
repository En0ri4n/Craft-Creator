package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.GetTaggeableContainerRecipeCreatorTileInfosServerPacket;
import fr.eno.craftcreator.packets.UpdateTaggeableContainerRecipeCreatorTilePacket;
import fr.eno.craftcreator.tileentity.TaggeableInventoryContainerTileEntity;
import fr.eno.craftcreator.utils.GuiList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TaggeableSlotsContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
    private Map<SlotItemHandler, ResourceLocation> taggedSlots;
    private GuiList<ResourceLocation> guiTagList;
    private SlotItemHandler selectedSlot;
    private final BlockPos pos;

    public TaggeableSlotsContainerScreen(T screenContainer, Inventory inv, Component titleIn, BlockPos pos)
    {
        super(screenContainer, inv, titleIn);
        this.taggedSlots = new HashMap<>();
        this.pos = pos;
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.getNetWork().send(PacketDistributor.SERVER.noArg(), new GetTaggeableContainerRecipeCreatorTileInfosServerPacket(this.pos, this.getMenu().containerId));

        this.guiTagList = new GuiList<>(this.leftPos, this.topPos + 1, 18);
        this.selectedSlot = null;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if(this.guiTagList.getKeys() != null)
            this.guiTagList.render(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int pMouseX, int pMouseY)
    {
        if(Screen.hasShiftDown() || Screen.hasControlDown()) drawString(matrixStack, this.font, References.getTranslate("screen.crafting.info").getString(), 0, this.imageHeight, 0x707370);
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

        if(slot instanceof SlotItemHandler && slot.getSlotIndex() != 9)
        {
            boolean checkInventory = ((SlotItemHandler) slot).getItemHandler() instanceof TaggeableInventoryContainerTileEntity;

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

            if(checkInventory && Screen.hasControlDown() && slot.getItem().getTags().findAny().isPresent())
            {
                this.selectedSlot = (SlotItemHandler) slot;
                this.guiTagList.setKeys(slot.getItem().getTags().map(TagKey::location).collect(Collectors.toList()));

                if(this.taggedSlots.containsKey(this.selectedSlot))
                    this.guiTagList.setSelectedKey(this.taggedSlots.get(this.selectedSlot));

                updateServerTileData();
                return true;
            }
            else if(checkInventory && Screen.hasControlDown() && slot.getItem().getTags().findAny().isPresent())
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

        if(mouseX <= this.leftPos || mouseX >= this.leftPos + this.imageWidth || mouseY <= this.topPos || mouseY >= this.topPos + this.imageHeight)
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
        for(int i = 0; i < this.getMenu().slots.size(); ++i)
        {
            Slot slot = this.getMenu().slots.get(i);

            if(this.isSlotSelected(slot, mouseX, mouseY) && slot.isActive())
            {
                return slot;
            }
        }

        return null;
    }

    private boolean isSlotSelected(Slot slotIn, double mouseX, double mouseY)
    {
        return this.isHovering(slotIn.x, slotIn.y, 16, 16, mouseX, mouseY);
    }

    public void setTaggedSlots(Map<Integer, ResourceLocation> taggedSlots)
    {
        Map<SlotItemHandler, ResourceLocation> taggedSlots1 = new HashMap<>();

        for(Integer integer : taggedSlots.keySet())
        {
            taggedSlots1.put((SlotItemHandler) this.getMenu().getSlot(integer), taggedSlots.get(integer));
        }

        this.taggedSlots = taggedSlots1;
    }

    public Map<SlotItemHandler, ResourceLocation> getTaggedSlots()
    {
        return this.taggedSlots;
    }

    @Override
    public void renderBackground(PoseStack pPoseStack)
    {
        super.renderBackground(pPoseStack);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float pPartialTick, int pMouseX, int pMouseY)
    {
        int x = this.leftPos;
        int y = this.topPos;

        for(Slot slot : this.taggedSlots.keySet())
        {
            if(slot instanceof SimpleSlotItemHandler simpleSlotItemHandler && simpleSlotItemHandler.isActive())
                fill(matrixStack, x + slot.x, y + slot.y, x + slot.x + 16, y + slot.y + 16, 0x8006e806);
        }

        if(this.selectedSlot != null)
            fill(matrixStack, x + selectedSlot.x, y + selectedSlot.y, x + selectedSlot.x + 16, y + selectedSlot.y + 16, 0xFFE7f50d);
    }

    @Override
    public void onClose()
    {
        updateServerTileData();
        super.onClose();
    }
}
