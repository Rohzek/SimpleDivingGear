package com.gmail.rohzek.dive.render;

import com.gmail.rohzek.dive.armor.SArmor;
import com.gmail.rohzek.dive.util.ConfigurationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class AirCounter 
{
	private static Minecraft mc = Minecraft.getInstance();
	private static float x = 0f, y = 0f;
	private static String display = "";
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Pre event)
	{
		if(ConfigurationManager.GENERAL.displayAirRemaining.get() && ConfigurationManager.GENERAL.consumeAir.get() && !mc.player.isCreative()) 
		{
			/*
			 * if (event.getType() == ElementType.AIR) { ItemStack chest =
			 * mc.player.getInventory().armor.get(2); // 0 = feet, 1 = legs, 2 = chest, 3 =
			 * head
			 * 
			 * if(chest.getItem() == SArmor.DIVE_CHEST) { long miliseconds =
			 * chest.getMaxDamage() - chest.getDamageValue(); long minutes = (miliseconds /
			 * 1000) / 60; long seconds = (miliseconds / 1000) % 60;
			 * 
			 * if(minutes > 0 || seconds > 0) { event.setCanceled(true); } } }
			 */
		}
	}
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event)
	{
		if(ConfigurationManager.GENERAL.displayAirRemaining.get() && ConfigurationManager.GENERAL.consumeAir.get() && !mc.player.isCreative()) 
		{
			if (event.getType() == ElementType.TEXT) 
			{	
				ItemStack chest = mc.player.getInventory().armor.get(2); // 0 = feet, 1 = legs, 2 = chest, 3 = head
				
				if(chest.getItem() == SArmor.DIVE_CHEST) 
				{
					long miliseconds = chest.getMaxDamage() - chest.getDamageValue();
	            	long minutes = (miliseconds / 1000) / 60;
	            	long seconds = (miliseconds / 1000) % 60;
					  
					  if(minutes == 0 && seconds == 0 && chest.getDamageValue() == chest.getMaxDamage() - 20) 
					  { 
						  display = I18n.get("display.simpledivegear.airempty");
						  // This int seems to be for the font color, in a hex format
						  mc.font.drawShadow(event.getMatrixStack(), display, getXCenter(display), getYCenter(display), Integer.parseInt("db8786", 16));
					  } 
					  else 
					  { 
						  String output = ": " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds); 
						  display = I18n.get("display.simpledivegear.airleft") + output;
						  // This int seems to be for the font color, in a hex format
						  mc.font.drawShadow(event.getMatrixStack(), display, getXCenter(display), getYCenter(display), Integer.parseInt("ffffff", 16));
					  }
				}
				else if(chest.getItem() == SArmor.NETHER_DIVE_CHEST) 
				{
					long miliseconds = chest.getMaxDamage() - chest.getDamageValue();
	            	long minutes = (miliseconds / 1000) / 60;
	            	long seconds = (miliseconds / 1000) % 60;
					  
					  if(minutes == 0 && seconds == 0 && chest.getDamageValue() == chest.getMaxDamage() - 20) 
					  { 
						  display = I18n.get("display.simpledivegear.coolantempty");
						  // This int seems to be for the font color, in a hex format
						  mc.font.drawShadow(event.getMatrixStack(), display, getXCenter(display), getYCenter(display), Integer.parseInt("db8786", 16));
					  } 
					  else 
					  { 
						  String output = ": " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds); 
						  display = I18n.get("display.simpledivegear.coolantleft") + output;
						  // This int seems to be for the font color, in a hex format
						  mc.font.drawShadow(event.getMatrixStack(), display, getXCenter(display), getYCenter(display), Integer.parseInt("ffffff", 16));
					  }
				}
			}
		}
	}
	
	public static float getXCenter(String text)
	{
		if(ConfigurationManager.GENERAL.airRemainingCustomLocation.get()) 
		{
			return ConfigurationManager.GENERAL.airDisplayCustomX.get();
		}
		
		x = mc.getWindow().getGuiScaledWidth();
		return (Float)((x - mc.font.width(text)) / 2);
	}
	
	public static float getYCenter(String text) 
	{
		if(ConfigurationManager.GENERAL.airRemainingCustomLocation.get()) 
		{
			return ConfigurationManager.GENERAL.airDisplayCustomY.get();
		}
		
		y = mc.getWindow().getGuiScaledHeight(); 
		return (Float) (y - ConfigurationManager.GENERAL.airDisplayVerticalAlignment.get());
	}
}