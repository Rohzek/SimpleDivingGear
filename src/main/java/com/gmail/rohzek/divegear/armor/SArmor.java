package com.gmail.rohzek.divegear.armor;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import com.gmail.rohzek.divegear.lib.DeferredRegistration;
import com.gmail.rohzek.divegear.lib.Reference;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class SArmor
{
	public static final Holder<ArmorMaterial> DIVE_GEAR_ARMOR_MATERIAL = DeferredRegistration.ARMOR_MATERIALS.register("divegear", () -> new ArmorMaterial(
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(Type.BOOTS, 0);
				map.put(Type.LEGGINGS, 0);
				map.put(Type.CHESTPLATE, 0);
				map.put(Type.HELMET, 0);
				map.put(Type.BODY, 0);
			}),
			9,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Tags.Items.LEATHERS),
			List.of(
					new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "leather"))
			),
			0f,
			0f
	));
	
	public static final Holder<ArmorMaterial> NETHER_DIVE_GEAR_ARMOR_MATERIAL = DeferredRegistration.ARMOR_MATERIALS.register("netherdivegear", () -> new ArmorMaterial(
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(Type.BOOTS, 0);
				map.put(Type.LEGGINGS, 0);
				map.put(Type.CHESTPLATE, 0);
				map.put(Type.HELMET, 0);
				map.put(Type.BODY, 0);
			}),
			9,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Tags.Items.INGOTS_NETHERITE),
			List.of(
					new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "netherite"))
			),
			0f,
			0f
	));
	
	public static final Supplier<ArmorItem> DIVE_HELMET = DeferredRegistration.ITEMS.register("divehelmet", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, Type.HELMET));
	public static final Supplier<ArmorItem> DIVE_HELMET_LIGHTS = DeferredRegistration.ITEMS.register("divehelmetlighted", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, Type.HELMET));
	public static final Supplier<ArmorItem> DIVE_CHEST = DeferredRegistration.ITEMS.register("divechest", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, Type.CHESTPLATE));
	public static final Supplier<ArmorItem> DIVE_LEGS = DeferredRegistration.ITEMS.register("divelegs", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, Type.LEGGINGS));
	public static final Supplier<ArmorItem> DIVE_BOOTS = DeferredRegistration.ITEMS.register("diveboots", () -> new SDiveGear(DIVE_GEAR_ARMOR_MATERIAL, Type.BOOTS));
	
	public static final Supplier<ArmorItem> NETHER_DIVE_HELMET = DeferredRegistration.ITEMS.register("netherdivehelmet", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, Type.HELMET));
	public static final Supplier<ArmorItem> NETHER_DIVE_HELMET_LIGHTS = DeferredRegistration.ITEMS.register("netherdivehelmetlighted", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, Type.HELMET));
	public static final Supplier<ArmorItem> NETHER_DIVE_CHEST = DeferredRegistration.ITEMS.register("netherdivechest", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, Type.CHESTPLATE));
	public static final Supplier<ArmorItem> NETHER_DIVE_LEGS = DeferredRegistration.ITEMS.register("netherdivelegs", () -> new SNetherDiveGear(NETHER_DIVE_GEAR_ARMOR_MATERIAL, Type.LEGGINGS));
	public static final Supplier<ArmorItem> NETHER_DIVE_BOOTS = DeferredRegistration.ITEMS.register("netherdiveboots", () -> new SNetherDiveGear(DIVE_GEAR_ARMOR_MATERIAL, Type.BOOTS));

	public static void register() {}
	
}
