package com.gmail.rohzek.dive.armor;

import com.gmail.rohzek.dive.lib.DeferredRegistration;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.registries.RegistryObject;

public class SArmor 
{
	public static final RegistryObject<SDiveGear> DIVE_HELMET = DeferredRegistration.ITEMS.register("divehelmet", () -> new SDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.HEAD));
	public static final RegistryObject<SDiveGear> DIVE_HELMET_LIGHTS = DeferredRegistration.ITEMS.register("divehelmetlighted", () -> new SDiveGear( DiveArmorMaterial.divegear, EquipmentSlot.HEAD));
	public static final RegistryObject<SDiveGear> DIVE_CHEST = DeferredRegistration.ITEMS.register("divechest", () -> new SDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.CHEST));
	public static final RegistryObject<SDiveGear> DIVE_LEGS = DeferredRegistration.ITEMS.register("divelegs", () -> new SDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.LEGS));
	public static final RegistryObject<SDiveGear> DIVE_BOOTS = DeferredRegistration.ITEMS.register("diveboots", () -> new SDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.FEET));
	
	public static final RegistryObject<SNetherDiveGear> NETHER_DIVE_HELMET = DeferredRegistration.ITEMS.register("netherdivehelmet", () -> new SNetherDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.HEAD));
	public static final RegistryObject<SNetherDiveGear> NETHER_DIVE_HELMET_LIGHTS = DeferredRegistration.ITEMS.register("netherdivehelmetlighted", () -> new SNetherDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.HEAD));
	public static final RegistryObject<SNetherDiveGear> NETHER_DIVE_CHEST = DeferredRegistration.ITEMS.register("netherdivechest", () -> new SNetherDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.CHEST));
	public static final RegistryObject<SNetherDiveGear> NETHER_DIVE_LEGS = DeferredRegistration.ITEMS.register("netherdivelegs", () -> new SNetherDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.LEGS));
	public static final RegistryObject<SNetherDiveGear> NETHER_DIVE_BOOTS = DeferredRegistration.ITEMS.register("netherdiveboots", () -> new SNetherDiveGear(DiveArmorMaterial.divegear, EquipmentSlot.FEET));
	
	public static void register() {}
}
