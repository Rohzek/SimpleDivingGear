package com.gmail.rohzek.dive.util;

import java.io.File;

import com.gmail.rohzek.dive.lib.Reference;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigurationManager
{
	public static File optionsLoc;
	
	public static boolean isDebug;
	
	public static boolean consumeAir;
	public static int timeToBreathe;
	
	public static boolean displayAirRemaining;
	
	public static String genCategory = "general";
	public static String debugCategory = "debug";
	public static String modCategory = "compatibility";
	public static String ovrCategory = "overrides";
	
	public ConfigurationManager(FMLPreInitializationEvent event)
	{
		optionsLoc = new File(Reference.LOCATION + "/" + Reference.MODID + ".cfg");
		
		Configuration optionsConfig = new Configuration(optionsLoc);
		options(optionsConfig);
	}
	

	private void options(Configuration config)
	{
		config.load();
		
		this.isDebug = config.get(debugCategory, "debugMode", false, "Enables more printouts to the chat. WARNING: Will spam the log file. Good for bug reports. Not recommended for regular play.").getBoolean(false);
		
		this.consumeAir = config.get(genCategory, "consumeAir", true, "If true, your air tank will drain while in use.").getBoolean(true);
		// Default minutes of air is 2, minimum is 1, maximum is 60
		this.timeToBreathe = config.getInt(genCategory, "minutesOfAir", 2, 1, 10, "Minutes of air in each tank");
		this.displayAirRemaining = config.get(genCategory, "displayAirRemaining", true, "If true, will draw the remaining air amount on screen when applicable.").getBoolean(true);
		
		config.save();
	}
}