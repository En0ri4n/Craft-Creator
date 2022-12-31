package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.blocks.*;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MOD_ID);

	public static final RegistryObject<Block> CRAFTING_TABLE_RECIPE_CREATOR = BLOCKS.register("crafting_table_recipe_creator", CraftingTableRecipeCreatorBlock::new);
	public static final RegistryObject<Block> FURNACE_RECIPE_CREATOR = BLOCKS.register("furnace_recipe_creator", FurnaceRecipeCreatorBlock::new);
	public static final RegistryObject<Block> STONE_CUTTER_RECIPE_CREATOR = BLOCKS.register("stone_cutter_recipe_creator", StoneCutterRecipeCreatorBlock::new);
	public static final RegistryObject<Block> SMITHING_TABLE_RECIPE_CREATOR = BLOCKS.register("smithing_table_recipe_creator", SmithingTableRecipeCreatorBlock::new);

	public static final RegistryObject<Block> BOTANIA_RECIPE_CREATOR;
    public static final RegistryObject<Block> THERMAL_RECIPE_CREATOR;

    static
	{
		BOTANIA_RECIPE_CREATOR = SupportedMods.BOTANIA.isLoaded() ? BLOCKS.register("botania_recipe_creator", BotaniaRecipeCreatorBlock::new) : null;
		THERMAL_RECIPE_CREATOR = SupportedMods.THERMAL.isLoaded() ? BLOCKS.register("thermal_recipe_creator", ThermalRecipeCreatorBlock::new) : null;
	}
}