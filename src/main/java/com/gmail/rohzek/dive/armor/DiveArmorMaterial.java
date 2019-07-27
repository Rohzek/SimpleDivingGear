package com.gmail.rohzek.dive.armor;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum DiveArmorMaterial implements IArmorMaterial
{
	divegear("divegear", -1, new int[] {0, 0, 0, 0}, 0, SoundEvents.BLOCK_PISTON_CONTRACT, 0f, Ingredient.fromItems(Blocks.OBSIDIAN));
	
	private String name;
	
	//Holds the maximum damage factor (each piece multiply this by it's own value) of the material, this is the item damage (how much can absorb before breaks)
	private int maxDamageFactor;
	
	// Holds the damage reduction (each 1 points is half a shield on gui) of each piece of armor (helmet, plate, legs and boots)
	private int[] damageReductionAmountArray;
	private int enchantability;
	private SoundEvent soundEvent;
	private float toughness;
	private Ingredient repairMaterial;
	
	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	
	private DiveArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, Ingredient ingredient) 
	{
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.toughness = toughness;
		this.repairMaterial = ingredient;
	}
	
	@Override
	public int getDurability(EntityEquipmentSlot slotIn) 
	{
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	@Override
	public int getDamageReductionAmount(EntityEquipmentSlot slotIn) 
	{
		return this.damageReductionAmountArray[slotIn.getIndex()];
	}

	@Override
	public int getEnchantability() 
	{
		return this.enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() 
	{
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairMaterial() 
	{
		return this.repairMaterial;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getName() 
	{
		return this.name;
	}

	@Override
	public float getToughness() 
	{
		return this.toughness;
	}
}
