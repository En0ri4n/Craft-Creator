package fr.eno.craftcreator.packets;

import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.api.ServerUtils;
import fr.eno.craftcreator.base.ModRecipeCreators;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.base.RecipeManagerDispatcher;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CreateRecipePacket
{
    private final SupportedMods mod;
    private final RecipeCreator recipeCreator;
    private final BlockPos tilePos;
    private final RecipeInfos recipeInfos;
    private final ModRecipeSerializer.SerializerType serializerType;

    public CreateRecipePacket(SupportedMods mod, RecipeCreator recipeCreator, BlockPos tilePos, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        this.mod = mod;
        this.recipeCreator = recipeCreator;
        this.tilePos = tilePos;
        this.recipeInfos = recipeInfos;
        this.serializerType = serializerType;
    }

    public static void encode(CreateRecipePacket msg, PacketBuffer buf)
    {
        buf.writeEnum(msg.mod);
        buf.writeUtf(msg.recipeCreator.getRecipeTypeLocation().toString());
        buf.writeBlockPos(msg.tilePos);
        buf.writeUtf(msg.recipeInfos.serialize().toString());
        buf.writeEnum(msg.serializerType);
    }

    public static CreateRecipePacket decode(PacketBuffer buf)
    {
        SupportedMods mod = buf.readEnum(SupportedMods.class);
        ResourceLocation recipeTypeLocation = CommonUtils.parse(buf.readUtf());
        RecipeCreator recipeCreator = ModRecipeCreators.byName(recipeTypeLocation);
        BlockPos tilePos = buf.readBlockPos();
        RecipeInfos recipeInfos = RecipeInfos.deserialize(buf.readUtf());
        ModRecipeSerializer.SerializerType serializerType = buf.readEnum(ModRecipeSerializer.SerializerType.class);

        return new CreateRecipePacket(mod, recipeCreator, tilePos, recipeInfos, serializerType);
    }

    public static class ServerHandler
    {
        public static void handle(CreateRecipePacket msg, Supplier<NetworkEvent.Context> ctx)
        {
            if(ServerUtils.getServerPlayer(ctx).containerMenu instanceof CommonContainer)
            {
                RecipeManagerDispatcher.createRecipe(msg.mod, msg.recipeCreator, ServerUtils.getBlockEntity(ctx, msg.tilePos), ((CommonContainer) ServerUtils.getServerPlayer(ctx).containerMenu).getContainerSlots(), msg.recipeInfos, msg.serializerType);
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
