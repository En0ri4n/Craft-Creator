package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveAddedRecipePacket
{
    private final SupportedMods mod;
    private final IRecipe<?> recipe;
    private final ModRecipeSerializer.SerializerType serializerType;

    public RemoveAddedRecipePacket(SupportedMods mod, IRecipe<?> recipe, ModRecipeSerializer.SerializerType serializerType)
    {
        this.mod = mod;
        this.recipe = recipe;
        this.serializerType = serializerType;
    }

    public static void encode(RemoveAddedRecipePacket msg, PacketBuffer buf)
    {
        buf.writeEnum(msg.mod);
        buf.writeResourceLocation(msg.recipe.getId());
        IRecipeSerializer serializer = msg.recipe.getSerializer();
        buf.writeResourceLocation(serializer.getRegistryName());
        serializer.toNetwork(buf, msg.recipe);
        buf.writeEnum(msg.serializerType);
    }

    public static RemoveAddedRecipePacket decode(PacketBuffer buf)
    {
        SupportedMods mod = buf.readEnum(SupportedMods.class);
        ResourceLocation recipeId = buf.readResourceLocation();
        IRecipeSerializer serializer = KubeJSHelper.getSerializer(buf.readResourceLocation());
        IRecipe<?> recipe = serializer.fromNetwork(recipeId, buf);
        ModRecipeSerializer.SerializerType serializerType = buf.readEnum(ModRecipeSerializer.SerializerType.class);

        return new RemoveAddedRecipePacket(mod, recipe, serializerType);
    }

    public static class ServerHandler
    {
        public static void handle(RemoveAddedRecipePacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ModRecipeCreatorDispatcher.getSeralizer(msg.mod.getModId()).removeAddedRecipeFrom(msg.mod, msg.recipe, msg.serializerType);

            ctx.get().setPacketHandled(true);
        }
    }
}
