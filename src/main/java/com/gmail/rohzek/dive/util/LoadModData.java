package com.gmail.rohzek.dive.util;

import com.gmail.rohzek.dive.lib.Reference;

import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * A replacement from having a MCMOD.info file, works exactly like one would though, just changed in code.
 * @author Rohzek
 *
 */
public class LoadModData 
{
	public static void load(FMLPreInitializationEvent PreEvent)
	{
		ModMetadata  m = PreEvent.getModMetadata();
		
		m.autogenerated = false; //This is required otherwise it will not work
		
		m.modId = Reference.MODID;
		m.version = Reference.VERSION;
		m.name = Reference.NAME;
		m.url = "https://discord.gg/TYAscSs";
		m.description = "A simple mod to make working in water a little easier";
		m.logoFile = Reference.RESOURCEID + "logo.png";
		LogHelper.debug("Our logo should be loaded from: " + Reference.RESOURCEID + "logo.png");
		m.authorList.add("Rohzek");
		m.authorList.add("CheshireRose");
		
	}
}