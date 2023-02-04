package fr.eno.craftcreator.container;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.slot.LockedSlot;
import fr.eno.craftcreator.container.slot.SimpleSlotItemHandler;
import fr.eno.craftcreator.container.utils.CommonContainer;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class MinecraftRecipeCreatorContainer extends CommonContainer
{
    public MinecraftRecipeCreatorContainer(int containerId, PlayerInventory inventory, PacketBuffer packet)
    {
        super(InitContainers.MINECRAFT_RECIPE_CREATOR.get(), containerId, inventory, packet);

        for(int i = 0; i < SlotHelper.MINECRAFT_SLOTS_SIZE; ++i)
        {
            if(PositionnedSlot.isValidSlot(SlotHelper.FURNACE_SLOTS, 2, i))
            {
                ItemStack fuel = new ItemStack(Items.EGG, 1);
                fuel.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 10);
                fuel.setHoverName(References.getTranslate("container.minecraft.furnace.fuel"));
                fuel.hideTooltipPart(ItemStack.TooltipDisplayFlags.ENCHANTMENTS);
                this.addSlot(new LockedSlot(tile, i, SlotHelper.MINECRAFT_SLOTS.get(i).getxPos(), SlotHelper.MINECRAFT_SLOTS.get(i).getyPos(), fuel));
            }

            this.addSlot(new SimpleSlotItemHandler(this.tile, i, SlotHelper.MINECRAFT_SLOTS.get(i).getxPos(), SlotHelper.MINECRAFT_SLOTS.get(i).getyPos()));
        }

        this.bindPlayerInventory(inventory);

        activeSlots(true);
    }

    @Override
    public SupportedMods getMod()
    {
        return SupportedMods.MINECRAFT;
    }
}