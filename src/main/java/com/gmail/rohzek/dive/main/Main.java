package com.gmail.rohzek.dive.main;

import com.gmail.rohzek.dive.creativetabs.STab;
import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;
import com.gmail.rohzek.dive.util.TimeOutput;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MODID)
public class Main 
{
	public static final CreativeModeTab DIVE_GEAR_TAB = new STab();
	
	public Main() 
	{
		// Register the mod
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		
		// Register configuration file
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigurationManager.spec);
	}
	
	@SubscribeEvent
	public void setup(FMLCommonSetupEvent event) 
	{
		LogHelper.log("Hello Minecraft, how are you? Did you know that Tony loves Amy? " + TimeOutput.getTimeTogether());
	}
}
