package fr.eno.craftcreator.utils;

import net.minecraft.util.ResourceLocation;

public enum CraftType
{
	CRAFTING_TABLE_SHAPED(new ResourceLocation("crafting_shaped")),
	CRAFTING_TABLE_SHAPELESS(new ResourceLocation("crafting_shapeless")),
	FURNACE_SMELTING(new ResourceLocation("smelting")),
	FURNACE_BLASTING(new ResourceLocation("blasting")),
	FURNACE_SMOKING(new ResourceLocation("smoking")),
	CAMPFIRE_COOKING(new ResourceLocation("campfire_cooking")),
	STONECUTTING(new ResourceLocation("stonecutting")),
	SMITHING_TABLE(new ResourceLocation("smithing"));

	private final ResourceLocation type;

	CraftType(ResourceLocation type)
	{
		this.type = type;
	}

	public ResourceLocation getType()
	{
		return type;
	}
	
	@Override
	public String toString()
	{
		return type.toString();
	}
}