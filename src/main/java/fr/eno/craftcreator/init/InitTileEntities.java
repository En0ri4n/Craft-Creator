package fr.eno.craftcreator.init;


import fr.eno.craftcreator.References;
import fr.eno.craftcreator.tileentity.BotaniaRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.CreateRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.MinecraftRecipeCreatorTile;
import fr.eno.craftcreator.tileentity.ThermalRecipeCreatorTile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class InitTileEntities
{
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.MOD_ID);

	public static final RegistryObject<BlockEntityType<?>> MINECRAFT_RECIPE_CREATOR = register("minecraft_recipe_creator", MinecraftRecipeCreatorTile::new, InitBlocks.MINECRAFT_RECIPE_CREATOR);
	public static final RegistryObject<BlockEntityType<?>> BOTANIA_RECIPE_CREATOR = register("botania_recipe_creator", BotaniaRecipeCreatorTile::new, InitBlocks.BOTANIA_RECIPE_CREATOR);
	public static final RegistryObject<BlockEntityType<?>> THERMAL_RECIPE_CREATOR = register("thermal_recipe_creator", ThermalRecipeCreatorTile::new, InitBlocks.THERMAL_RECIPE_CREATOR);
	public static final RegistryObject<BlockEntityType<?>> CREATE_RECIPE_CREATOR = register("create_recipe_creator", CreateRecipeCreatorTile::new, InitBlocks.CREATE_RECIPE_CREATOR);

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<?>> register(String registryName, BlockEntityType.BlockEntitySupplier<? extends T> tileEntity, Supplier<Block> block)
	{
		return TILE_ENTITIES.register(registryName, () -> BlockEntityType.Builder.of(tileEntity, block.get()).build(null));
	}
}