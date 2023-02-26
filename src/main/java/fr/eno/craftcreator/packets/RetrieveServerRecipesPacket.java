package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.api.ServerUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSHelper;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class RetrieveServerRecipesPacket
{
    private final SupportedMods mod;
    private final InitPackets.RecipeList recipeList;
    private final ModRecipeSerializer.SerializerType serializerType;

    public RetrieveServerRecipesPacket(SupportedMods mod, InitPackets.RecipeList recipeList, ModRecipeSerializer.SerializerType serializerType)
    {
        this.mod = mod;
        this.recipeList = recipeList;
        this.serializerType = serializerType;
    }

    public static void encode(RetrieveServerRecipesPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeEnum(msg.mod);
        packetBuffer.writeEnum(msg.recipeList);
        packetBuffer.writeEnum(msg.serializerType);
    }

    public static RetrieveServerRecipesPacket decode(PacketBuffer packetBuffer)
    {
        SupportedMods mod = packetBuffer.readEnum(SupportedMods.class);
        InitPackets.RecipeList recipeList = packetBuffer.readEnum(InitPackets.RecipeList.class);
        ModRecipeSerializer.SerializerType serializerType = packetBuffer.readEnum(ModRecipeSerializer.SerializerType.class);
        return new RetrieveServerRecipesPacket(mod, recipeList, serializerType);
    }

    public static class ServerHandler
    {
        /**
         * Handle the packet on the server side and send the data to the client using {@link UpdateRecipeCreatorTileDataClientPacket}
         */
        public static void handle(RetrieveServerRecipesPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            CommonUtils.serverTask(() ->
            {
                if(msg.serializerType == ModRecipeSerializer.SerializerType.KUBE_JS)
                {
                    List<KubeJSModifiedRecipe> recipes = KubeJSHelper.getModifiedRecipes(msg.mod);
                    for(KubeJSModifiedRecipe recipe : recipes)
                    {
                        InitPackets.NetworkHelper.sendToPlayer(ServerUtils.getServerPlayer(ctx), new UpdateRecipeListClientPacket(msg.mod, msg.recipeList, recipe.serialize()));
                    }
                }
            });
        }
    }
}
