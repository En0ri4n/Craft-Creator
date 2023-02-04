package fr.eno.craftcreator.init;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.blocks.BotaniaRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.MinecraftRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.ThermalRecipeCreatorBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MOD_ID);

	public static final RegistryObject<Block> MINECRAFT_RECIPE_CREATOR = BLOCKS.register("minecraft_recipe_creator", MinecraftRecipeCreatorBlock::new);
	public static final RegistryObject<Block> BOTANIA_RECIPE_CREATOR = BLOCKS.register("botania_recipe_creator", BotaniaRecipeCreatorBlock::new);
    public static final RegistryObject<Block> THERMAL_RECIPE_CREATOR = BLOCKS.register("thermal_recipe_creator", ThermalRecipeCreatorBlock::new);
}