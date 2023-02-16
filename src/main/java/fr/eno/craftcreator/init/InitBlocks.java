package fr.eno.craftcreator.init;


import fr.eno.craftcreator.References;
import fr.eno.craftcreator.blocks.BotaniaRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.CreateRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.MinecraftRecipeCreatorBlock;
import fr.eno.craftcreator.blocks.ThermalRecipeCreatorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MOD_ID);

	public static final RegistryObject<Block> MINECRAFT_RECIPE_CREATOR = BLOCKS.register("minecraft_recipe_creator", MinecraftRecipeCreatorBlock::new);
	public static final RegistryObject<Block> BOTANIA_RECIPE_CREATOR = BLOCKS.register("botania_recipe_creator", BotaniaRecipeCreatorBlock::new);
    public static final RegistryObject<Block> THERMAL_RECIPE_CREATOR = BLOCKS.register("thermal_recipe_creator", ThermalRecipeCreatorBlock::new);
	public static final RegistryObject<Block> CREATE_RECIPE_CREATOR = BLOCKS.register("create_recipe_creator", CreateRecipeCreatorBlock::new);
}