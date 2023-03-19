package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveRecipePacket
{
    private final SupportedMods mod;
    private final KubeJSModifiedRecipe modifiedRecipe;
    private final ModRecipeSerializer.SerializerType serializerType;

    public RemoveRecipePacket(SupportedMods mod, KubeJSModifiedRecipe modifiedRecipe, ModRecipeSerializer.SerializerType serializerType)
    {

        this.mod = mod;
        this.modifiedRecipe = modifiedRecipe;
        this.serializerType = serializerType;
    }

    public static void encode(RemoveRecipePacket msg, PacketBuffer buf)
    {
        buf.writeEnum(msg.mod);
        buf.writeUtf(msg.modifiedRecipe.serialize().toString());
        buf.writeEnum(msg.serializerType);
    }

    public static RemoveRecipePacket decode(PacketBuffer buf)
    {
        SupportedMods mod = buf.readEnum(SupportedMods.class);
        KubeJSModifiedRecipe modifiedRecipe = KubeJSModifiedRecipe.deserialize(buf.readUtf());
        ModRecipeSerializer.SerializerType serializerType = buf.readEnum(ModRecipeSerializer.SerializerType.class);

        return new RemoveRecipePacket(mod, modifiedRecipe, serializerType);
    }

    public static class ServerHandler
    {
        public static void handle(RemoveRecipePacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            ModRecipeCreatorDispatcher.getSeralizer(msg.mod.getModId()).removeRecipe(msg.modifiedRecipe, msg.serializerType);

            ctx.get().setPacketHandled(true);
        }
    }
}
