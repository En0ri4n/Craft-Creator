package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.container.slot.CustomSlotItemHandler;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.screen.widgets.GuiList;
import fr.eno.craftcreator.tileentity.utils.TaggeableInventoryContainerTileEntity;
import fr.eno.craftcreator.utils.PositionnedSlot;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TaggeableSlotsContainerScreen<T extends Container> extends ModRecipeCreatorDataScreen<T>
{
    private final Map<SlotItemHandler, ResourceLocation> taggedSlots;
    protected final List<Integer> nbtSlots;

    private GuiList<ResourceLocation> guiTagList;
    private SimpleCheckBox nbtCheckBox;
    private SlotItemHandler selectedSlot;
    private final BlockPos pos;

    protected int leftPos;
    protected int topPos;
    protected int imageWidth;
    protected int imageHeight;

    public TaggeableSlotsContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, BlockPos pos)
    {
        super(screenContainer, inv, titleIn);
        this.taggedSlots = new HashMap<>();
        this.nbtSlots = new ArrayList<>();
        this.pos = pos;
        this.imageWidth = xSize;
        this.imageHeight = ySize;
    }

    public TaggeableSlotsContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        this(screenContainer, inv, titleIn, null);
    }

    @Override
    protected void init()
    {
        super.init();

        this.leftPos = guiLeft;
        this.topPos = guiTop;

        if(this.pos != null)
        {
            InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("tagged_slots", this.pos, InitPackets.PacketDataType.MAP_INT_STRING));
            InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("nbt_slots", this.pos, InitPackets.PacketDataType.INT_ARRAY));
        }

        addListener(this.nbtCheckBox = new SimpleCheckBox(this.leftPos + 5, this.topPos + 5, 10, 10, References.getTranslate("screen.crafting.info.nbt"), false, checkBox ->
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
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if(this.guiTagList.getKeys() != null) this.guiTagList.render(matrixStack, mouseX, mouseY);
        if(this.selectedSlot != null) this.nbtCheckBox.render(matrixStack, mouseX, mouseY, partialTicks);

        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int pMouseX, int pMouseY)
    {
        if(Screen.hasShiftDown() || Screen.hasControlDown())
            drawString(matrixStack, this.font, References.getTranslate("screen.crafting.info.msg").getString(), 0, this.ySize, 0x707370);
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

        if(slot instanceof CustomSlotItemHandler && PositionnedSlot.contains(this.getTaggeableSlots(), slot.getSlotIndex()))
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

            if(checkInventory && Screen.hasControlDown() && slot.getStack().getItem().getTags().stream().findFirst().isPresent())
            {
                this.selectedSlot = (SlotItemHandler) slot;
                this.guiTagList.setKeys(new ArrayList<>(slot.getStack().getItem().getTags()));
                this.nbtCheckBox.setSelected(this.nbtSlots.contains(slot.getSlotIndex()));

                if(this.taggedSlots.containsKey(this.selectedSlot))
                    this.guiTagList.setSelectedKey(this.taggedSlots.get(this.selectedSlot));

                return true;
            }
            else if(checkInventory && Screen.hasControlDown() && slot.getHasStack())
            {
                this.selectedSlot = (SlotItemHandler) slot;
                this.nbtCheckBox.setSelected(this.nbtSlots.contains(slot.getSlotIndex()));
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
        for(int i = 0; i < this.getContainer().inventorySlots.size(); ++i)
        {
            Slot slot = this.getContainer().inventorySlots.get(i);

            if(this.isSlotSelected(slot, mouseX, mouseY) && slot.isEnabled())
            {
                return slot;
            }
        }

        return null;
    }

    private boolean isSlotSelected(Slot slotIn, double mouseX, double mouseY)
    {
        int x = this.leftPos;
        int y = this.topPos;

        return ClientUtils.isMouseHover(x + slotIn.xPos, y + slotIn.yPos, (int) mouseX, (int) mouseY, 16, 16);
    }

    public void setTaggedSlots(Map<Integer, ResourceLocation> taggedSlots)
    {
        for(Integer integer : taggedSlots.keySet())
        {
            this.taggedSlots.put((SlotItemHandler) this.getContainer().getSlot(integer), taggedSlots.get(integer));
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
    protected void renderHoveredTooltip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        super.renderHoveredTooltip(poseStack, mouseX, mouseY);

        if(this.selectedSlot != null)
        {
            this.nbtCheckBox.renderToolTip(poseStack, mouseX, mouseY);
        }

        int x = this.leftPos;
        int y = this.topPos;

        for(Slot slot : this.getContainer().inventorySlots)
        {
            if(slot instanceof SimpleSlotItemHandler)
            {
                SimpleSlotItemHandler simpleSlotItemHandler = (SimpleSlotItemHandler) slot;
                if(simpleSlotItemHandler.isEnabled() && this.taggedSlots.containsKey(simpleSlotItemHandler) && this.nbtSlots.contains(simpleSlotItemHandler.getSlotIndex()))
                {
                    fill(poseStack, x + slot.xPos, y + slot.yPos, x + slot.xPos + 16, y + slot.yPos + 16, 0x8000FFFF);
                }
                else if(simpleSlotItemHandler.isEnabled() && this.taggedSlots.containsKey(simpleSlotItemHandler))
                    fill(poseStack, x + slot.xPos, y + slot.yPos, x + slot.xPos + 16, y + slot.yPos + 16, 0x8000FF00);
                else if(simpleSlotItemHandler.isEnabled() && this.nbtSlots.contains(simpleSlotItemHandler.getSlotIndex()))
                    fill(poseStack, x + slot.xPos, y + slot.yPos, x + slot.xPos + 16, y + slot.yPos + 16, 0x800000FF);
            }
        }

        if(this.selectedSlot != null)
            fill(poseStack, x + selectedSlot.xPos, y + selectedSlot.yPos, x + selectedSlot.xPos + 16, y + selectedSlot.yPos + 16, 0xFFE7f50d);

    }

    @Override
    public void renderBackground(MatrixStack pPoseStack)
    {
        super.renderBackground(pPoseStack);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int offset)
    {
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
