package com.gmail.rohzek.dive.items;

import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.main.Main;

import net.minecraft.item.Item;

public class DiveItem extends Item
{
	public DiveItem(String name) 
	{
		super(new Item.Properties().group(Main.DIVE_GEAR_TAB).maxStackSize(1));
		
		setNames(name);
	}
	
	void setNames(String name) 
	{
		setRegistryName(Reference.MODID, name);
	}
}
