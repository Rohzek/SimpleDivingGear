package com.gmail.rohzek.dive.armor;

import com.gmail.rohzek.dive.lib.Reference;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MODID)
public class SArmor 
{
	public static SDiveGear DIVE_HELMET = new SDiveGear("divehelmet", DiveArmorMaterial.divegear, EquipmentSlotType.HEAD);
	public static SDiveGear DIVE_HELMET_LIGHTS = new SDiveGear("divehelmetlighted", DiveArmorMaterial.divegear, EquipmentSlotType.HEAD);
	public static SDiveGear DIVE_CHEST = new SDiveGear("divechest", DiveArmorMaterial.divegear, EquipmentSlotType.CHEST);
	public static SDiveGear DIVE_LEGS = new SDiveGear("divelegs", DiveArmorMaterial.divegear, EquipmentSlotType.LEGS);
	public static SDiveGear DIVE_BOOTS = new SDiveGear("diveboots", DiveArmorMaterial.divegear, EquipmentSlotType.FEET);
	
	public static SNetherDiveGear NETHER_DIVE_HELMET = new SNetherDiveGear("netherdivehelmet", DiveArmorMaterial.divegear, EquipmentSlotType.HEAD);
	public static SNetherDiveGear NETHER_DIVE_HELMET_LIGHTS = new SNetherDiveGear("netherdivehelmetlighted", DiveArmorMaterial.divegear, EquipmentSlotType.HEAD);
	public static SNetherDiveGear NETHER_DIVE_CHEST = new SNetherDiveGear("netherdivechest", DiveArmorMaterial.divegear, EquipmentSlotType.CHEST);
	public static SNetherDiveGear NETHER_DIVE_LEGS = new SNetherDiveGear("netherdivelegs", DiveArmorMaterial.divegear, EquipmentSlotType.LEGS);
	public static SNetherDiveGear NETHER_DIVE_BOOTS = new SNetherDiveGear("netherdiveboots", DiveArmorMaterial.divegear, EquipmentSlotType.FEET);
	
	
	@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler 
	{
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) 
		{
			event.getRegistry().registerAll
			(
					DIVE_HELMET_LIGHTS,
					DIVE_HELMET,
					DIVE_CHEST,
					DIVE_LEGS,
					DIVE_BOOTS,
					NETHER_DIVE_HELMET_LIGHTS,
					NETHER_DIVE_HELMET,
					NETHER_DIVE_CHEST,
					NETHER_DIVE_LEGS,
					NETHER_DIVE_BOOTS
			);
		}
	}
}
