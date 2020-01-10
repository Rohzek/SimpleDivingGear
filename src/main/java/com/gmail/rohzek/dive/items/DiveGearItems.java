package com.gmail.rohzek.dive.items;

import com.gmail.rohzek.dive.lib.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MODID)
public class DiveGearItems 
{
    public static final Item DIVE_HELMET_CORE = new DiveItem("divehelmetcore");
    
    @Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler 
	{
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) 
		{
			event.getRegistry().registerAll
			(
					DIVE_HELMET_CORE
			);
		}
	}
}