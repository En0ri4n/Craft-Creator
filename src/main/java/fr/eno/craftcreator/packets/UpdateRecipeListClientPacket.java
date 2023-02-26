package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.screen.RecipeManagerScreen;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.utils.CustomRunnable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateRecipeListClientPacket
{
    private final SupportedMods mod;
    private final InitPackets.RecipeList recipeList;
    private final CompoundTag serializedRecipe;

    public UpdateRecipeListClientPacket(SupportedMods mod, InitPackets.RecipeList recipeList, CompoundTag serializedRecipe)
    {
        this.mod = mod;
        this.recipeList = recipeList;
        this.serializedRecipe = serializedRecipe;
    }

    public static void encode(UpdateRecipeListClientPacket msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeEnum(msg.mod);
        packetBuffer.writeEnum(msg.recipeList);
        packetBuffer.writeNbt(msg.serializedRecipe);
    }

    public static UpdateRecipeListClientPacket decode(FriendlyByteBuf packetBuffer)
    {
        SupportedMods mod = packetBuffer.readEnum(SupportedMods.class);
        InitPackets.RecipeList recipeList = packetBuffer.readEnum(InitPackets.RecipeList.class);
        CompoundTag serializedRecipe = packetBuffer.readNbt();
        return new UpdateRecipeListClientPacket(mod, recipeList, serializedRecipe);
    }

    public static class ClientHandler
    {
        public static void handle(UpdateRecipeListClientPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            CommonUtils.clientTask(CustomRunnable.of(() ->
            {
                if(ClientUtils.getCurrentScreen() instanceof RecipeManagerScreen)
                {
                    ((RecipeManagerScreen) ClientUtils.getCurrentScreen()).addToList(msg.recipeList, new SimpleListWidget.ModifiedRecipeEntry(KubeJSModifiedRecipe.deserialize(msg.serializedRecipe)));
                }
            }));
        }
    }
}
