package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.init.InitPackets;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateRecipeListClientPacket
{
    private final SupportedMods mod;
    private final InitPackets.RecipeList recipeList;
    private final String recipeId;
    private final String serializedRecipe;

    public UpdateRecipeListClientPacket(SupportedMods mod, InitPackets.RecipeList recipeList, String recipeId, String serializedRecipe)
    {
        this.mod = mod;
        this.recipeList = recipeList;
        this.recipeId = recipeId;
        this.serializedRecipe = serializedRecipe;
    }

    public static void encode(UpdateRecipeListClientPacket msg, PacketBuffer packetBuffer)
    {
        packetBuffer.writeEnum(msg.mod);
        packetBuffer.writeEnum(msg.recipeList);
        packetBuffer.writeUtf(msg.recipeId);
        packetBuffer.writeUtf(msg.serializedRecipe);
    }

    public static UpdateRecipeListClientPacket decode(PacketBuffer packetBuffer)
    {
        SupportedMods mod = packetBuffer.readEnum(SupportedMods.class);
        InitPackets.RecipeList recipeList = packetBuffer.readEnum(InitPackets.RecipeList.class);
        String recipeId = packetBuffer.readUtf();
        String serializedRecipe = packetBuffer.readUtf();

        return new UpdateRecipeListClientPacket(mod, recipeList, recipeId, serializedRecipe);
    }

    public static class ClientHandler
    {
        public static void handle(UpdateRecipeListClientPacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ClientUtils.addToList(msg.recipeList, msg.recipeId, msg.serializedRecipe);
            ctx.get().setPacketHandled(true);
        }
    }
}
