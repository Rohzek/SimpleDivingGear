package com.gmail.rohzek.dive.items;

import com.gmail.rohzek.dive.main.Main;

import net.minecraft.world.item.Item;

public class DiveItem extends Item
{
	public DiveItem() 
	{
		super(new Item.Properties().tab(Main.DIVE_GEAR_TAB).stacksTo(1));
	}
}
