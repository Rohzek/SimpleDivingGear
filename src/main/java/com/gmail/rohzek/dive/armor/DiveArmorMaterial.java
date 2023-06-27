package com.gmail.rohzek.dive.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum DiveArmorMaterial implements ArmorMaterial
{
	divegear("divegear", -1, new int[] {0, 0, 0, 0}, 0, SoundEvents.PISTON_CONTRACT, 0f, Ingredient.of(new ItemStack(Blocks.OBSIDIAN)));
	
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
		this.soundEvent = soundEvent; // This broke the entirety of the armor!
		this.toughness = toughness;
		this.repairMaterial = ingredient;
	}
	
	@Override
	public int getDurabilityForType(Type type) 
	{
		return MAX_DAMAGE_ARRAY[type.getSlot().getIndex()] * this.maxDamageFactor;
	}
	
	@Override
	public int getDefenseForType(Type type) 
	{
		return this.damageReductionAmountArray[type.getSlot().getIndex()];
	}

	@Override
	public int getEnchantmentValue() 
	{
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() 
	{
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairIngredient() 
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

	@Override
	public float getKnockbackResistance() 
	{
		return 0;
	}
}
