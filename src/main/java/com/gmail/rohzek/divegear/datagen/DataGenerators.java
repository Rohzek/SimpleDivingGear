package com.gmail.rohzek.divegear.datagen;

import java.util.concurrent.CompletableFuture;

import com.gmail.rohzek.divegear.lib.Reference;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Reference.MODID,bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators 
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) 
	{
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput packOutput = dataGenerator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		dataGenerator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
		dataGenerator.addProvider(event.includeServer(), new ModItemModelProvider(packOutput, existingFileHelper));
	}
}
