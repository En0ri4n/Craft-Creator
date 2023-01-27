package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.ThermalRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.vanilla.CraftingTableRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.vanilla.FurnaceRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.vanilla.SmithingTableRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.vanilla.StoneCutterRecipeCreatorTile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitTileEntities
{
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.MOD_ID);

	public static final RegistryObject<BlockEntityType<?>> CRAFTING_TABLE_RECIPE_CREATOR = TILE_ENTITY.register("crafting_table_recipe_creator", () -> BlockEntityType.Builder.of(CraftingTableRecipeCreatorTile::new, InitBlocks.CRAFTING_TABLE_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<?>> FURNACE_RECIPE_CREATOR = TILE_ENTITY.register("furnace_recipe_creator", () -> BlockEntityType.Builder.of(FurnaceRecipeCreatorTile::new, InitBlocks.FURNACE_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<?>> STONE_CUTTER_RECIPE_CREATOR = TILE_ENTITY.register("stone_cutter_recipe_creator", () -> BlockEntityType.Builder.of(StoneCutterRecipeCreatorTile::new, InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<?>> SMITHING_TABLE_RECIPE_CREATOR = TILE_ENTITY.register("smithing_table_recipe_creator", () -> BlockEntityType.Builder.of(SmithingTableRecipeCreatorTile::new, InitBlocks.SMITHING_TABLE_RECIPE_CREATOR.get()).build(null));

	public static final RegistryObject<BlockEntityType<?>> BOTANIA_RECIPE_CREATOR;
	public static final RegistryObject<BlockEntityType<?>> THERMAL_RECIPE_CREATOR;

	static
	{
		BOTANIA_RECIPE_CREATOR = SupportedMods.BOTANIA.isLoaded() ? TILE_ENTITY.register("botania_recipe_creator", () -> BlockEntityType.Builder.of(BotaniaRecipeCreatorTile::new, InitBlocks.BOTANIA_RECIPE_CREATOR.get()).build(null)) : null;
		THERMAL_RECIPE_CREATOR = SupportedMods.THERMAL.isLoaded() ? TILE_ENTITY.register("thermal_recipe_creator", () -> BlockEntityType.Builder.of(ThermalRecipeCreatorTile::new, InitBlocks.THERMAL_RECIPE_CREATOR.get()).build(null)) : null;
	}
}