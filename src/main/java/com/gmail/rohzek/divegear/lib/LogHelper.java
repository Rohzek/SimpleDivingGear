package com.gmail.rohzek.divegear.lib;

import com.gmail.rohzek.divegear.SimpleDivingGear;

public class LogHelper 
{
	public static void Log(String message) 
	{
		SimpleDivingGear.LOGGER.info(message);
	}
	
	public static void Debug(String message) 
	{
		if(ConfigurationManager.GENERAL.isDebug.get()) 
		{
			SimpleDivingGear.LOGGER.debug(message);
		}
	}
}
