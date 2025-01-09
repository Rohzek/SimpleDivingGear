package com.gmail.rohzek.divegear;

import org.slf4j.Logger;

import com.gmail.rohzek.divegear.armor.SArmor;
import com.gmail.rohzek.divegear.items.DiveGearItems;
import com.gmail.rohzek.divegear.lib.ConfigurationManager;
import com.gmail.rohzek.divegear.lib.DeferredRegistration;
import com.gmail.rohzek.divegear.lib.Reference;
import com.gmail.rohzek.divegear.render.AirCounter;
import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Reference.MODID)
public class SimpleDivingGear 
{
	public final static Logger LOGGER = LogUtils.getLogger();
	
	@SuppressWarnings("unused")
	public SimpleDivingGear(IEventBus modEventBus, ModContainer modContainer)
	{
		// Register the mod
		//modEventBus.register(this);
		
		if (FMLEnvironment.dist == Dist.CLIENT) 
		{
			modEventBus.addListener(AirCounter.INSTANCE::onRegisterOverlays);
        }
		
		// Register items
		DeferredRegistration.register(modEventBus);
		DiveGearItems.register();
		SArmor.register();

		// Register configuration file
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		modContainer.registerConfig(ModConfig.Type.COMMON, ConfigurationManager.spec);
		
		/**
		 * To use any wooden planks use:
		 * "tag": "minecraft:planks"
		 * To use any stone that can be used to make tools:
		 * "tag": "minecraft:stone_tool_materials"
		 */
	}
}
