package com.gmail.rohzek.creativetabs;

import java.util.Collections;
import java.util.Comparator;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class STab extends CreativeTabs
{
	private ItemStack iconItem;
	
	private ItemSorter itemSorter = new ItemSorter();
	
	public STab(String label, Item item)
	{
		super(label);
		iconItem = new ItemStack(item);
	}
	
	public STab(String label, Block block)
	{
		super(label);
		iconItem = new ItemStack(block);
	}

	@Override
	public ItemStack getTabIconItem() 
	{
		return iconItem;
	}
	
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> items) 
	{
		super.displayAllRelevantItems(items);
		
		Collections.sort(items, itemSorter);
	}
	
	// Sorts items in alphabetical order using their display names
	private static class ItemSorter implements Comparator<ItemStack> 
	{
		
		@Override
		public int compare(ItemStack o1, ItemStack o2) 
		{
			Item item1 = o1.getItem();
			Item item2 = o2.getItem();
			
			if (((item1 instanceof ItemBlock)) && (!(item2 instanceof ItemBlock))) 
			{
				return -1;
			}
			
			if (((item2 instanceof ItemBlock)) && (!(item1 instanceof ItemBlock)))
			{
				return 1;
			}
			
			String displayName1 = o1.getDisplayName();
			String displayName2 = o2.getDisplayName();
			
			int result = displayName1.compareToIgnoreCase(displayName2);
			
			return result;
		}
	}
}
