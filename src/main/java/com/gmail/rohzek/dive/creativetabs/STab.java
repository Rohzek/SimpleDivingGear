package com.gmail.rohzek.dive.creativetabs;

import com.gmail.rohzek.dive.armor.SArmor;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class STab extends CreativeModeTab
{
	public STab() 
	{
		super("diveGearTab");
	}

	@Override
	public ItemStack makeIcon() 
	{
		return SArmor.DIVE_HELMET.get().getDefaultInstance();
		//return new ItemStack(SArmor.DIVE_HELMET);
	}
}
