package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitTileEntities
{
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.MOD_ID);

	public static final RegistryObject<TileEntityType<?>> CRAFTING_TABLE_RECIPE_CREATOR = TILE_ENTITY.register("crafting_table_recipe_creator", () -> TileEntityType.Builder.create(CraftingTableRecipeCreatorTile::new, InitBlocks.CRAFTING_TABLE_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<TileEntityType<?>> FURNACE_RECIPE_CREATOR = TILE_ENTITY.register("furnace_recipe_creator", () -> TileEntityType.Builder.create(FurnaceRecipeCreatorTile::new, InitBlocks.FURNACE_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<TileEntityType<?>> STONE_CUTTER_RECIPE_CREATOR = TILE_ENTITY.register("stone_cutter_recipe_creator", () -> TileEntityType.Builder.create(StoneCutterRecipeCreatorTile::new, InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<TileEntityType<?>> SMITHING_TABLE_RECIPE_CREATOR = TILE_ENTITY.register("smithing_table_recipe_creator", () -> TileEntityType.Builder.create(SmithingTableRecipeCreatorTile::new, InitBlocks.SMITHING_TABLE_RECIPE_CREATOR.get()).build(null));

	public static final RegistryObject<TileEntityType<?>> BOTANIA_RECIPE_CREATOR;

	static
	{
		BOTANIA_RECIPE_CREATOR = SupportedMods.isBotaniaLoaded() ? TILE_ENTITY.register("botania_recipe_creator", () -> TileEntityType.Builder.create(BotaniaRecipeCreatorTile::new, InitBlocks.BOTANIA_RECIPE_CREATOR.get()).build(null)) : null;
	}
}