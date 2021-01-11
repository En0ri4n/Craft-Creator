package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.blocks.CraftingTableRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.FurnaceRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.SmithingTableRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.StoneCutterRecipeCreatorBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MOD_ID);

	public static final RegistryObject<Block> CRAFTING_TABLE_RECIPE_CREATOR = BLOCKS.register("crafting_table_recipe_creator", () -> new CraftingTableRecipeCreatorBlock());
	public static final RegistryObject<Block> FURNACE_RECIPE_CREATOR = BLOCKS.register("furnace_recipe_creator", () -> new FurnaceRecipeCreatorBlock());
	public static final RegistryObject<Block> STONE_CUTTER_RECIPE_CREATOR = BLOCKS.register("stone_cutter_recipe_creator", () -> new StoneCutterRecipeCreatorBlock());
	public static final RegistryObject<Block> SMITHING_TABLE_RECIPE_CREATOR = BLOCKS.register("smithing_table_recipe_creator", () -> new SmithingTableRecipeCreatorBlock());
}