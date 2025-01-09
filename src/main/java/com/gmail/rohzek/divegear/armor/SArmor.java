package com.gmail.rohzek.divegear.armor;

import java.util.EnumMap;
import java.util.function.Supplier;

import com.gmail.rohzek.divegear.lib.DeferredRegistration;
import com.gmail.rohzek.divegear.lib.Reference;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.common.Tags;

public class SArmor
{
	public static final ArmorMaterial DIVE_GEAR_ARMOR_MATERIAL = new ArmorMaterial(
			15, 
			Util.make(new EnumMap<>(ArmorType.class), map -> {
				map.put(ArmorType.BOOTS, 0);
				map.put(ArmorType.LEGGINGS, 0);
				map.put(ArmorType.CHESTPLATE, 0);
				map.put(ArmorType.HELMET, 0);
				map.put(ArmorType.BODY, 0);
			}), 
			9, 
			SoundEvents.ARMOR_EQUIP_GENERIC, 
			0,
			0, 
			Tags.Items.LEATHERS, 
			ResourceLocation.fromNamespaceAndPath(Reference.MODID, "divegear"));
	
	public static final ArmorMaterial NETHER_DIVE_GEAR_ARMOR_MATERIAL = new ArmorMaterial(
			15, 
			Util.make(new EnumMap<>(ArmorType.class), map -> {
				map.put(ArmorType.BOOTS, 0);
				map.put(ArmorType.LEGGINGS, 0);
				map.put(ArmorType.CHESTPLATE, 0);
				map.put(ArmorType.HELMET, 0);
				map.put(ArmorType.BODY, 0);
			}), 
			9, 
			SoundEvents.ARMOR_EQUIP_GENERIC, 
			0,
			0,
			Tags.Items.INGOTS_NETHERITE, 
			ResourceLocation.fromNamespaceAndPath(Reference.MODID, "netherdivegear"));
	
	public static final Supplier<ArmorItem> DIVE_HELMET = DeferredRegistration.ITEMS.register("divehelmet", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, ArmorType.HELMET, "divehelmet"));
	public static final Supplier<ArmorItem> DIVE_HELMET_LIGHTS = DeferredRegistration.ITEMS.register("divehelmetlighted", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, ArmorType.HELMET, "divehelmetlighted"));
	public static final Supplier<ArmorItem> DIVE_CHEST = DeferredRegistration.ITEMS.register("divechest", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, ArmorType.CHESTPLATE, "divechest"));
	public static final Supplier<ArmorItem> DIVE_LEGS = DeferredRegistration.ITEMS.register("divelegs", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, ArmorType.LEGGINGS, "divelegs"));
	public static final Supplier<ArmorItem> DIVE_BOOTS = DeferredRegistration.ITEMS.register("diveboots", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, ArmorType.BOOTS, "diveboots"));
	
	public static final Supplier<ArmorItem> NETHER_DIVE_HELMET = DeferredRegistration.ITEMS.register("netherdivehelmet", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, ArmorType.HELMET, "netherdivehelmet"));
	public static final Supplier<ArmorItem> NETHER_DIVE_HELMET_LIGHTS = DeferredRegistration.ITEMS.register("netherdivehelmetlighted", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, ArmorType.HELMET, "netherdivehelmetlighted"));
	public static final Supplier<ArmorItem> NETHER_DIVE_CHEST = DeferredRegistration.ITEMS.register("netherdivechest", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, ArmorType.CHESTPLATE, "netherdivechest"));
	public static final Supplier<ArmorItem> NETHER_DIVE_LEGS = DeferredRegistration.ITEMS.register("netherdivelegs", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, ArmorType.LEGGINGS, "netherdivelegs"));
	public static final Supplier<ArmorItem> NETHER_DIVE_BOOTS = DeferredRegistration.ITEMS.register("netherdiveboots", () -> new SNetherDiveGear(DIVE_GEAR_ARMOR_MATERIAL, ArmorType.BOOTS, "netherdiveboots"));

	public static void register() {}
	
}
