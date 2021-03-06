package com.gmail.rohzek.dive.render;

import com.gmail.rohzek.dive.armor.SArmor;
import com.gmail.rohzek.dive.util.ConfigurationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
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
			if (event.getType() == ElementType.AIR) 
			{	
				ItemStack chest = mc.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
				
				if(chest.getItem() == SArmor.DIVE_CHEST) 
				{
					long miliseconds = chest.getMaxDamage() - chest.getDamage();
	            	long minutes = (miliseconds / 1000) / 60;
	            	long seconds = (miliseconds / 1000) % 60;
	            	
	            	if(minutes > 0 || seconds > 0) 
	            	{
	            		event.setCanceled(true);
	            	}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event)
	{
		if(ConfigurationManager.GENERAL.displayAirRemaining.get() && ConfigurationManager.GENERAL.consumeAir.get() && !mc.player.isCreative()) 
		{
			if (event.getType() == ElementType.TEXT) 
			{	
				ItemStack chest = mc.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
				
				if(chest.getItem() == SArmor.DIVE_CHEST) 
				{
					long miliseconds = chest.getMaxDamage() - chest.getDamage();
	            	long minutes = (miliseconds / 1000) / 60;
	            	long seconds = (miliseconds / 1000) % 60;
	            	
	            	Matrix4f matrix4f = event.getMatrixStack().getLast().getMatrix();
	            	IRenderTypeBuffer buffer = mc.getRenderTypeBuffers().getBufferSource();
	            	
	            	if(minutes == 0 && seconds == 0 && chest.getDamage() == chest.getMaxDamage() - 20) 
	            	{
	            		display = I18n.format("display.simpledivegear.airempty");
	            		mc.fontRenderer.renderString(display, getXCenter(display), getYCenter(display), 0, true, matrix4f, buffer, false, 0, 9);
	            	}
	            	else
	            	{
	            		String output = ": " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds);
	            		display = I18n.format("display.simpledivegear.airleft") + output;
	            		mc.fontRenderer.renderString(display, getXCenter(display), getYCenter(display), 0, true, matrix4f, buffer, false, 0, 9); 
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
		
		x = mc.getMainWindow().getScaledWidth();
		return ((x - mc.fontRenderer.getStringWidth(text)) / 2);
	}
	
	public static float getYCenter(String text) 
	{
		if(ConfigurationManager.GENERAL.airRemainingCustomLocation.get()) 
		{
			return ConfigurationManager.GENERAL.airDisplayCustomY.get();
		}
		
		y = mc.getMainWindow().getScaledHeight();
		return (Float) (y - ConfigurationManager.GENERAL.airDisplayVerticalAlignment.get());
	}
}