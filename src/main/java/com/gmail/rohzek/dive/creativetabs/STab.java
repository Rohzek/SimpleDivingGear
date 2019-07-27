package com.gmail.rohzek.dive.creativetabs;

import com.gmail.rohzek.dive.armor.SArmor;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class STab extends ItemGroup
{
	public STab() 
	{
		super("diveGearTab");
	}

	@Override
	public ItemStack createIcon() 
	{
		return new ItemStack(SArmor.DIVE_HELMET);
	}
}
