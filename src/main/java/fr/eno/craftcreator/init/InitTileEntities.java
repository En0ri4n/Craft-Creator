package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.*;
import net.minecraft.Util;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class InitTileEntities
{
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.MOD_ID);

	public static final RegistryObject<BlockEntityType<?>> CRAFTING_TABLE_RECIPE_CREATOR = TILE_ENTITY.register("crafting_table_recipe_creator", () -> BlockEntityType.Builder.of(CraftingTableRecipeCreatorTile::new, InitBlocks.CRAFTING_TABLE_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<?>> FURNACE_RECIPE_CREATOR = TILE_ENTITY.register("furnace_recipe_creator", () -> BlockEntityType.Builder.of(FurnaceRecipeCreatorTile::new, InitBlocks.FURNACE_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<?>> STONE_CUTTER_RECIPE_CREATOR = TILE_ENTITY.register("stone_cutter_recipe_creator", () -> BlockEntityType.Builder.of(StoneCutterRecipeCreatorTile::new, InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<?>> SMITHING_TABLE_RECIPE_CREATOR = TILE_ENTITY.register("smithing_table_recipe_creator", () -> BlockEntityType.Builder.of(SmithingTableRecipeCreatorTile::new, InitBlocks.SMITHING_TABLE_RECIPE_CREATOR.get()).build(null));

	public static final RegistryObject<BlockEntityType<?>> BOTANIA_RECIPE_CREATOR;

	static
	{
		BOTANIA_RECIPE_CREATOR = SupportedMods.isBotaniaLoaded() ? TILE_ENTITY.register("botania_recipe_creator", () -> BlockEntityType.Builder.of(BotaniaRecipeCreatorTile::new, InitBlocks.BOTANIA_RECIPE_CREATOR.get()).build(null)) : null;
	}
}