package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.screen.widgets.GuiList;
import fr.eno.craftcreator.tileentity.vanilla.TaggeableInventoryContainerTileEntity;
import fr.eno.craftcreator.utils.PositionnedSlot;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TaggeableSlotsContainerScreen<T extends AbstractContainerMenu> extends ModRecipeCreatorDataScreen<T>
{
    private final Map<SlotItemHandler, ResourceLocation> taggedSlots;
    protected final List<Integer> nbtSlots;

    private GuiList<ResourceLocation> guiTagList;
    private SimpleCheckBox nbtCheckBox;
    private SlotItemHandler selectedSlot;
    private final BlockPos pos;

    public TaggeableSlotsContainerScreen(T screenContainer, Inventory inv, Component titleIn, BlockPos pos)
    {
        super(screenContainer, inv, titleIn);
        this.taggedSlots = new HashMap<>();
        this.nbtSlots = new ArrayList<>();
        this.pos = pos;
    }

    public TaggeableSlotsContainerScreen(T screenContainer, Inventory inv, Component titleIn)
    {
        this(screenContainer, inv, titleIn, null);
    }

    @Override
    protected void init()
    {
        super.init();

        if(this.pos != null)
        {
            InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("tagged_slots", this.pos, InitPackets.PacketDataType.MAP_INT_STRING));
            InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("nbt_slots", this.pos, InitPackets.PacketDataType.INT_ARRAY));
        }

        addWidget(this.nbtCheckBox = new SimpleCheckBox(this.leftPos + 5, this.topPos + 5, 10, 10, References.getTranslate("screen.crafting.info.nbt"), false, checkBox ->
        {
            if(this.selectedSlot != null)
            {
                if(this.nbtSlots.contains(this.selectedSlot.getSlotIndex()))
                    this.nbtSlots.removeIf(slotIndex -> slotIndex == this.selectedSlot.getSlotIndex());
                else if(!this.nbtSlots.contains(this.selectedSlot.getSlotIndex()))
                    this.nbtSlots.add(this.selectedSlot.getSlotIndex());
            }
        }));
        this.guiTagList = new GuiList<>(this.leftPos, this.topPos + 1, 18);
        this.selectedSlot = null;
    }

    @Override
    public void setData(String dataName, Object data)
    {
        if(dataName.equals("tagged_slots"))
        {
            setTaggedSlots((Map<Integer, ResourceLocation>) data);
        }
        else if(dataName.equals("nbt_slots"))
        {
            setNbtSlots((int[]) data);
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if(this.guiTagList.getKeys() != null) this.guiTagList.render(matrixStack, mouseX, mouseY);
        if(this.selectedSlot != null) this.nbtCheckBox.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int pMouseX, int pMouseY)
    {
        if(Screen.hasShiftDown() || Screen.hasControlDown())
            drawString(matrixStack, this.font, References.getTranslate("screen.crafting.info.msg").getString(), 0, this.imageHeight, 0x707370);
    }

    protected abstract List<PositionnedSlot> getTaggeableSlots();

    private void updateServerTileData()
    {
        if(this.pos != null)
        {
            InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("tagged_slots", this.pos, InitPackets.PacketDataType.MAP_INT_STRING, getTagged(this.taggedSlots)));
            InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("nbt_slots", this.pos, InitPackets.PacketDataType.INT_ARRAY, getNbtSlots(this.nbtSlots)));
        }
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

        if(slot instanceof SlotItemHandler && PositionnedSlot.contains(this.getTaggeableSlots(), slot.getSlotIndex()))
        {
            boolean checkInventory = ((SlotItemHandler) slot).getItemHandler() instanceof TaggeableInventoryContainerTileEntity;

            if(checkInventory && !Screen.hasControlDown())
            {
                this.guiTagList.setKeys(null);
                this.selectedSlot = null;
                this.taggedSlots.remove(slot);
                if(this.nbtSlots.contains(slot.getSlotIndex())) this.nbtSlots.removeIf(slotIndex -> slotIndex == slot.getSlotIndex());
                return super.mouseClicked(mouseX, mouseY, button);
            }

            this.guiTagList.setKeys(null);
            this.guiTagList.setSelectedKey(null);
            this.selectedSlot = null;

            if(checkInventory && Screen.hasControlDown() && slot.getItem().getTags().findAny().isPresent())
            {
                this.selectedSlot = (SlotItemHandler) slot;
                this.guiTagList.setKeys(slot.getItem().getTags().map(TagKey::location).collect(Collectors.toList()));
                this.nbtCheckBox.setSelected(this.nbtSlots.contains(slot.getSlotIndex()));

                if(this.taggedSlots.containsKey(this.selectedSlot))
                    this.guiTagList.setSelectedKey(this.taggedSlots.get(this.selectedSlot));

                return true;
            }
            else if(checkInventory && Screen.hasControlDown() && slot.getItem().getTags().findAny().isPresent())
            {
                this.guiTagList.setKeys(null);
                this.guiTagList.setSelectedKey(null);
                this.selectedSlot = null;
                return true;
            }
        }

        if(nbtCheckBox.mouseClicked(mouseX, mouseY, button))
        {
            return true;
        }

        this.guiTagList.mouseClicked((int) mouseX, (int) mouseY, resourceLocation ->
        {
            if(resourceLocation == null)
            {
                this.taggedSlots.remove(this.selectedSlot);
                return;
            }

            this.taggedSlots.put(this.selectedSlot, resourceLocation);
        });

        this.guiTagList.setKeys(null);
        this.guiTagList.setSelectedKey(null);
        this.selectedSlot = null;
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
        for(Integer integer : taggedSlots.keySet())
        {
            this.taggedSlots.put((SlotItemHandler) this.getMenu().getSlot(integer), taggedSlots.get(integer));
        }
    }

    private void setNbtSlots(int[] data)
    {
        this.nbtSlots.clear();
        for(int i : data)
        {
            this.nbtSlots.add(i);
        }
    }

    public Map<SlotItemHandler, ResourceLocation> getTaggedSlots()
    {
        return this.taggedSlots;
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY)
    {
        super.renderTooltip(poseStack, mouseX, mouseY);

        if(this.selectedSlot != null)
        {
            this.nbtCheckBox.renderToolTip(poseStack, mouseX, mouseY);
        }
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

        for(Slot slot : this.getMenu().slots)
        {
            if(slot instanceof SimpleSlotItemHandler simpleSlotItemHandler)
            {
                if(simpleSlotItemHandler.isActive() && this.taggedSlots.containsKey(simpleSlotItemHandler) && this.nbtSlots.contains(simpleSlotItemHandler.getSlotIndex()))
                {
                    fill(matrixStack, x + slot.x, y + slot.y, x + slot.x + 16, y + slot.y + 16, 0x8000FFFF);
                }
                else if(simpleSlotItemHandler.isActive() && this.taggedSlots.containsKey(simpleSlotItemHandler))
                    fill(matrixStack, x + slot.x, y + slot.y, x + slot.x + 16, y + slot.y + 16, 0x8000FF00);
                else if(simpleSlotItemHandler.isActive() && this.nbtSlots.contains(simpleSlotItemHandler.getSlotIndex()))
                    fill(matrixStack, x + slot.x, y + slot.y, x + slot.x + 16, y + slot.y + 16, 0x800000FF);
            }
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

    protected int[] getNbtSlots(List<Integer> nbtSlots)
    {
        int[] slots = new int[nbtSlots.size()];
        for(int i = 0; i < nbtSlots.size(); i++)
        {
            slots[i] = nbtSlots.get(i);
        }
        return slots;
    }
}
