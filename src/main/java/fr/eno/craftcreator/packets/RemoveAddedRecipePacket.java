package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveAddedRecipePacket
{
    private final SupportedMods mod;
    private final ResourceLocation recipeId;
    private final ModRecipeSerializer.SerializerType serializerType;

    public RemoveAddedRecipePacket(SupportedMods mod, ResourceLocation recipeId, ModRecipeSerializer.SerializerType serializerType)
    {
        this.mod = mod;
        this.recipeId = recipeId;
        this.serializerType = serializerType;
    }

    public static void encode(RemoveAddedRecipePacket msg, FriendlyByteBuf buf)
    {
        buf.writeEnum(msg.mod);
        buf.writeResourceLocation(msg.recipeId);
        buf.writeEnum(msg.serializerType);
    }

    public static RemoveAddedRecipePacket decode(FriendlyByteBuf buf)
    {
        SupportedMods mod = buf.readEnum(SupportedMods.class);
        ResourceLocation recipeId = buf.readResourceLocation();
        ModRecipeSerializer.SerializerType serializerType = buf.readEnum(ModRecipeSerializer.SerializerType.class);

        return new RemoveAddedRecipePacket(mod, recipeId, serializerType);
    }

    public static class ServerHandler
    {
        public static void handle(RemoveAddedRecipePacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ModRecipeCreatorDispatcher.getSeralizer(msg.mod.getModId()).removeAddedRecipeFrom(msg.mod, CraftCreator.SERVER.getRecipeManager().byKey(msg.recipeId).get(), msg.serializerType);

            ctx.get().setPacketHandled(true);
        }
    }
}
