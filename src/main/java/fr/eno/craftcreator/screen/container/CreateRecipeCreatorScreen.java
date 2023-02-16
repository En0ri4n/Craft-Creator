package fr.eno.craftcreator.screen.container;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<CreateRecipeCreatorContainer>
{
    public CreateRecipeCreatorScreen(CreateRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
    }

    @Override
    protected void initFields()
    {

    }

    @Override
    protected void initWidgets()
    {

    }

    @Override
    protected void retrieveExtraData()
    {

    }

    @Override
    protected RecipeInfos getExtraRecipeInfos(RecipeInfos recipeInfos)
    {
        return recipeInfos;
    }

    @Override
    protected void updateGui()
    {

    }

    @Override
    protected void renderGui(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {

    }

    @Override
    protected Item getRecipeIcon(ModRecipeCreator modRecipeCreator)
    {
        switch(modRecipeCreator)
        {
            case CRUSHING:
                return ForgeRegistries.ITEMS.getValue(ClientUtils.parse("create:crushing_wheel"));
            default:
                return Items.COMMAND_BLOCK;
        }
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return SlotHelper.CREATE_SLOTS_INPUT;
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return new ArrayList<>();
    }
}
