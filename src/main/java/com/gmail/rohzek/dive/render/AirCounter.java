package com.gmail.rohzek.dive.render;

import com.gmail.rohzek.dive.armor.SArmor;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(value = Side.CLIENT)
public class AirCounter 
{
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ScaledResolution res;
	private static float x = 0f, y = 0f;
	private static float topOfGUI = 69f;
	private static String display = "";

	public AirCounter() 
	{
		res = new ScaledResolution(mc);
	}
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event)
	{
		if(ConfigurationManager.displayAirRemaining && ConfigurationManager.consumeAir) 
		{
			if (event.getType() == ElementType.TEXT) 
			{	
				ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				
				if(chest.getItem() == SArmor.DIVE_CHEST) 
				{
					long miliseconds = chest.getMaxDamage() - chest.getItemDamage();
	            	long minutes = (miliseconds / 1000) / 60;
	            	long seconds = (miliseconds / 1000) % 60;
	                
	            	if(minutes == 0 && seconds == 0 && chest.getItemDamage() == chest.getMaxDamage() - 20) 
	            	{
	            		display = "Air Tank Empty";
	            		mc.fontRenderer.drawString(display, getXCenter(display), getYCenter(display), 0xFFFFFFFF, true);
	            	}
	            	else
	            	{
	            		display = "Air Left: " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds);
	            		mc.fontRenderer.drawString(display, getXCenter(display), getYCenter(display), 0xFFFFFFFF, true);
	            	}
				}
			}
		}
	}
	
	public static float getXCenter(String text)
	{
		res = new ScaledResolution(mc);
		x = res.getScaledWidth();
		return ((x - mc.fontRenderer.getStringWidth(text)) / 2);
	}
	
	public static float getYCenter(String text) 
	{
		res = new ScaledResolution(mc);
		y = res.getScaledHeight();
		return (y - topOfGUI);
	}
	
}
