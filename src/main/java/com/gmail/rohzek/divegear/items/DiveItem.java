package com.gmail.rohzek.divegear.items;

import com.gmail.rohzek.divegear.lib.Reference;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class DiveItem extends Item
{
	public DiveItem(String itemname) 
	{
		super(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Reference.MODID, itemname))));
	}
}
