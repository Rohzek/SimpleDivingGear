package com.gmail.rohzek.dive.lib;

import com.gmail.rohzek.dive.armor.SArmor;
import com.gmail.rohzek.dive.items.DiveGearItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DeferredRegistration 
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
	
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MODID);
	
	public static final RegistryObject<CreativeModeTab> DIVE_GEAR = TABS.register("divegeartab", () -> CreativeModeTab.builder()
			// Set name of tab to display
			.title(Component.translatable("itemgroup." + Reference.MODID + ".divegeartab"))
			// Set icon of creative tab
			//.icon(() -> new ItemStack(DiveGearItems.DIVE_HELMET_CORE.get()))
			.icon(() -> new ItemStack(DiveGearItems.DIVE_HELMET_CORE.get()))
			// Adds items to tab
			.displayItems((params, output) -> 
			{
				output.accept(DiveGearItems.DIVE_HELMET_CORE.get());
				
				// Create enchanted versions of our items
				ItemStack diveHelmet = new ItemStack(SArmor.DIVE_HELMET.get());
				diveHelmet.enchant(Enchantments.RESPIRATION, 1);
				ItemStack diveHelmetLight = new ItemStack(SArmor.DIVE_HELMET_LIGHTS.get());
				diveHelmetLight.enchant(Enchantments.RESPIRATION, 1);
				ItemStack diveChest = new ItemStack(SArmor.DIVE_CHEST.get());
				diveChest.enchant(Enchantments.AQUA_AFFINITY, 1);
				ItemStack diveLegs = new ItemStack(SArmor.DIVE_LEGS.get());
				diveLegs.enchant(Enchantments.SWIFT_SNEAK, 1);
				ItemStack diveBoots = new ItemStack(SArmor.DIVE_BOOTS.get());
				diveBoots.enchant(Enchantments.DEPTH_STRIDER, 1);
				
				ItemStack diveHelmetNether = new ItemStack(SArmor.NETHER_DIVE_HELMET.get());
				diveHelmetNether.enchant(Enchantments.FIRE_PROTECTION, 1);
				ItemStack diveHelmetLightNether = new ItemStack(SArmor.NETHER_DIVE_HELMET_LIGHTS.get());
				diveHelmetLightNether.enchant(Enchantments.FIRE_PROTECTION, 1);
				ItemStack diveChestNether = new ItemStack(SArmor.NETHER_DIVE_CHEST.get());
				diveChestNether.enchant(Enchantments.FIRE_PROTECTION, 1);
				ItemStack diveLegsNether = new ItemStack(SArmor.NETHER_DIVE_LEGS.get());
				diveLegsNether.enchant(Enchantments.FIRE_PROTECTION, 1);
				ItemStack diveBootsNether = new ItemStack(SArmor.NETHER_DIVE_BOOTS.get());
				diveBootsNether.enchant(Enchantments.FIRE_PROTECTION, 1);
				
				// Add enchanted versions to the tab
				output.accept(diveHelmet);
				output.accept(diveHelmetLight);
				output.accept(diveChest);
				output.accept(diveLegs);
				output.accept(diveBoots);
				
				output.accept(diveHelmetNether);
				output.accept(diveHelmetLightNether);
				output.accept(diveChestNether);
				output.accept(diveLegsNether);
				output.accept(diveBootsNether);
			})
			// Finalize builder
			.build()
	);
	
	public static void register(IEventBus bus) 
	{		
		ITEMS.register(bus);
		TABS.register(bus);
	}
}
