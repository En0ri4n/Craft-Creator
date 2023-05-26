package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveModifiedRecipe
{
    private final SupportedMods mod;
    private final KubeJSModifiedRecipe modifiedRecipe;

    public RemoveModifiedRecipe(SupportedMods mod, KubeJSModifiedRecipe modifiedRecipe)
    {
        this.mod = mod;
        this.modifiedRecipe = modifiedRecipe;
    }

    public static void encode(RemoveModifiedRecipe msg, FriendlyByteBuf buf)
    {
        buf.writeEnum(msg.mod);
        buf.writeUtf(msg.modifiedRecipe.serialize().toString());
    }

    public static RemoveModifiedRecipe decode(FriendlyByteBuf buf)
    {
        SupportedMods mod = buf.readEnum(SupportedMods.class);
        KubeJSModifiedRecipe modifiedRecipe = KubeJSModifiedRecipe.deserialize(buf.readUtf());

        return new RemoveModifiedRecipe(mod, modifiedRecipe);
    }

    public static class ServerHandler
    {
        public static void handle(RemoveModifiedRecipe msg, Supplier<NetworkEvent.Context> ctx)
        {
            ModRecipeSerializer.removeModifiedRecipe(msg.mod, msg.modifiedRecipe);

            ctx.get().setPacketHandled(true);
        }
    }
}
