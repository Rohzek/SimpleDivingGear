package com.gmail.rohzek.dive.main;

import java.io.File;

import com.gmail.rohzek.dive.creativetabs.STab;
import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.proxy.CommonProxy;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LoadModData;
import com.gmail.rohzek.dive.util.LogHelper;
import com.gmail.rohzek.dive.util.TimeOutput;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Main 
{
	@Instance(Reference.MODID)
	public static Main simplefoodrebalance;
	
	@SidedProxy(clientSide = Reference.CLIENTSIDEPROXY, serverSide = Reference.SERVERSIDEPROXY)
	public static CommonProxy proxy;
	
	public static final STab S_TAB = new STab("diveGearTab", Blocks.GLASS);
	
	@EventHandler
	public static void PreLoad(FMLPreInitializationEvent preEvent)
	{
		LogHelper.log("Hello Minecraft, how are you? Did you know that Tony loves Amy? " + TimeOutput.getTimeTogether());
		
		LogHelper.debug("Beginning Pre-Initialization");
		Reference.LOCATION = new File(preEvent.getModConfigurationDirectory().getAbsolutePath() + "/" + Reference.MODID);
		
		LogHelper.debug("Loading MCMOD replacement info");
		LoadModData.load(preEvent);
		
		// Configuration file loader
		//ConfigurationManager manager = new ConfigurationManager(preEvent);
		
		LogHelper.debug("Pre-Initialization Complete");
	}
	
	@EventHandler
	public static void load(FMLInitializationEvent event)
	{
		LogHelper.debug("Beginning Initialization");
		LogHelper.debug("Initialization Complete");
	}
	
	@EventHandler
	public static void PostLoad(FMLPostInitializationEvent postEvent)
	{
		LogHelper.log("Checking for compatibility modules");
		
		LogHelper.debug("Adding smelting recipes");
	}
}
