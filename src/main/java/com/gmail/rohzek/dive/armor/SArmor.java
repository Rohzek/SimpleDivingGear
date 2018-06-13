package com.gmail.rohzek.dive.armor;

import com.gmail.rohzek.dive.lib.Reference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class SArmor 
{
	public static ArmorMaterial DIVEARMOR = EnumHelper.addArmorMaterial("DiveGear", Reference.RESOURCEID + "divegear", -1, new int[] {1, 1, 1, 1}, 0, SoundEvents.BLOCK_PISTON_CONTRACT, 0f);
	public static SDiveGear DIVE_HELMET = new SDiveGear("divehelmet", DIVEARMOR, 1, EntityEquipmentSlot.HEAD);
	public static SDiveGear DIVE_HELMET_LIGHTS = new SDiveGear("divehelmetlighted", DIVEARMOR, 1, EntityEquipmentSlot.HEAD);
	public static SDiveGear DIVE_CHEST = new SDiveGear("divechest", DIVEARMOR, 1, EntityEquipmentSlot.CHEST);
	public static SDiveGear DIVE_LEGS = new SDiveGear("divelegs", DIVEARMOR, 2, EntityEquipmentSlot.LEGS);
	public static SDiveGear DIVE_BOOTS = new SDiveGear("diveboots", DIVEARMOR, 1, EntityEquipmentSlot.FEET);
	
	public static void registerOreDicts() {}
	
	public static void registerOreDict(String name, Item item) 
	{
		OreDictionary.registerOre(name, item);
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler 
	{
		@SubscribeEvent
		public static void registerRenders(ModelRegistryEvent event)
		{
			registerRender(DIVE_HELMET_LIGHTS);
			registerRender(DIVE_HELMET);
			registerRender(DIVE_CHEST);
			registerRender(DIVE_LEGS);
			registerRender(DIVE_BOOTS);
		}
		
		public static void registerRender(Item item)
		{	
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
		
		public static void registerRender(Item item, int meta, String addon)
		{
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName() + addon, "inventory"));
		}
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) 
		{
			final Item[] items =
			{
				DIVE_HELMET_LIGHTS,
				DIVE_HELMET,
				DIVE_CHEST,
				DIVE_LEGS,
				DIVE_BOOTS,
			};
			
			final IForgeRegistry<Item> registry = event.getRegistry();
			
			for (final Item item : items) 
			{
				registry.register(item);
			}
		}
	}
}
