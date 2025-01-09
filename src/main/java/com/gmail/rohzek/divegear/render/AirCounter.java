package com.gmail.rohzek.divegear.render;

import com.gmail.rohzek.divegear.armor.SArmor;
import com.gmail.rohzek.divegear.lib.ConfigurationManager;
import com.gmail.rohzek.divegear.lib.Reference;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public class AirCounter implements LayeredDraw.Layer
{
	public static AirCounter INSTANCE = new AirCounter();
	
	public void onRegisterOverlays(RegisterGuiLayersEvent event) 
	{
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "overlay"), INSTANCE);
    }
	
	@SuppressWarnings("resource")
	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) 
	{
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		
		if(ConfigurationManager.GENERAL.consumeAir.get() && ConfigurationManager.GENERAL.displayAirRemaining.get() && !player.isCreative()) 
		{
			NonNullList<ItemStack> armor = player.getInventory().armor;
			ItemStack chest = armor.get(2);
			
			if(chest.getItem().equals(SArmor.DIVE_CHEST.get()) || chest.getItem().equals(SArmor.NETHER_DIVE_CHEST.get())) 
			{
				String display = "";
				Font font = minecraft.font;
				int color = 0;
				long miliseconds = chest.getMaxDamage() - chest.getDamageValue();
	        	long minutes = (miliseconds / 1000) / 60;
	        	long seconds = (miliseconds / 1000) % 60;
	        	String output = ": " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds); 
	        	
	        	if(chest.getItem().equals(SArmor.DIVE_CHEST.get())) 
				{
	        		if(minutes == 0 && seconds == 0 && chest.getDamageValue() == chest.getMaxDamage() - 20) 
		        	{ 
		        		display = I18n.get("display.simpledivegear.airempty");
		        		color = Integer.parseInt("db8786", 16);
					}
		        	else 
		        	{ 
		        		display = I18n.get("display.simpledivegear.airleft") + output;
		        		color = Integer.parseInt("ffffff", 16);
		        	}
				}
				
				if(chest.getItem().equals(SArmor.NETHER_DIVE_CHEST.get())) 
				{	        	
		        	if(minutes == 0 && seconds == 0 && chest.getDamageValue() == chest.getMaxDamage() - 20) 
		        	{ 
		        		display = I18n.get("display.simpledivegear.coolantempty");
		        		color = Integer.parseInt("db8786", 16);
					}
		        	else 
		        	{
		        		display = I18n.get("display.simpledivegear.coolantleft") + output;
		        		color = Integer.parseInt("ffffff", 16);
		        	}
				}
				
				guiGraphics.drawString(font, display, getXCenter(minecraft, font, display), getYCenter(minecraft), color);
			}
		}
	}
	
	int getXCenter(Minecraft mc, Font font, String display) 
	{
		if(ConfigurationManager.GENERAL.airRemainingCustomLocation.get()) 
		{
			return ConfigurationManager.GENERAL.airDisplayCustomX.get();
		}
		
		return (mc.getWindow().getGuiScaledWidth() - font.width(display)) / 2;
	}
	
	int getYCenter(Minecraft mc) 
	{
		if(ConfigurationManager.GENERAL.airRemainingCustomLocation.get()) 
		{
			return ConfigurationManager.GENERAL.airDisplayCustomY.get();
		}
		
		return mc.getWindow().getGuiScaledHeight() - ConfigurationManager.GENERAL.airDisplayVerticalAlignment.get();
	}
}
