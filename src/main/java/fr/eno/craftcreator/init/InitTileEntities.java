package fr.eno.craftcreator.init;

import com.mojang.datafixers.types.Type;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.CreateRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.MinecraftRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.ThermalRecipeCreatorTile;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class InitTileEntities
{
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.MOD_ID);

	public static final RegistryObject<TileEntityType<?>> MINECRAFT_RECIPE_CREATOR = register("minecraft_recipe_creator", MinecraftRecipeCreatorTile::new, InitBlocks.MINECRAFT_RECIPE_CREATOR);
	public static final RegistryObject<TileEntityType<?>> BOTANIA_RECIPE_CREATOR = register("botania_recipe_creator", BotaniaRecipeCreatorTile::new, InitBlocks.BOTANIA_RECIPE_CREATOR);
	public static final RegistryObject<TileEntityType<?>> THERMAL_RECIPE_CREATOR = register("thermal_recipe_creator", ThermalRecipeCreatorTile::new, InitBlocks.THERMAL_RECIPE_CREATOR);
	public static final RegistryObject<TileEntityType<?>> CREATE_RECIPE_CREATOR = register("create_recipe_creator", CreateRecipeCreatorTile::new, InitBlocks.CREATE_RECIPE_CREATOR);

	private static <T extends TileEntity> RegistryObject<TileEntityType<?>> register(String registryName, Supplier<T> tileEntity, Supplier<Block> block)
	{
		Type<?> type = Util.fetchChoiceType(TypeReferences.BLOCK_ENTITY, registryName);
		return TILE_ENTITIES.register(registryName, () -> TileEntityType.Builder.of(tileEntity, block.get()).build(type));
	}
}