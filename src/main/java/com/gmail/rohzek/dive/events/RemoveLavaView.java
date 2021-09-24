package com.gmail.rohzek.dive.events;

import com.gmail.rohzek.dive.armor.SArmor;
import com.gmail.rohzek.dive.util.LogHelper;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class RemoveLavaView 
{
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void removeLavaView(EntityViewRenderEvent.FogDensity event) 
	{
		Entity entity = event.getInfo().getEntity();
		
		if(entity instanceof LivingEntity) 
		{
			LivingEntity lEntity = (LivingEntity) entity;
			
			if(lEntity instanceof PlayerEntity) 
			{
				PlayerEntity player = (PlayerEntity) lEntity;
				
				if(player.isInLava())
				{
					Block above = player.level.getBlockState(new BlockPos(player.getX(), player.getY() + 2, player.getZ())).getBlock();

					if(above == Blocks.LAVA) 
					{
						NonNullList<ItemStack> armorSlots = player.inventory.armor;
						
						ItemStack head = armorSlots.get(3);
						
						if(head.getItem().equals(SArmor.NETHER_DIVE_HELMET) || head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS)) 
						{
							LogHelper.debug("Remove that lava view!");
							
							RenderSystem.fogStart(0.0F);
					        RenderSystem.fogEnd(1024F);
					        event.setDensity(0.05F);
					        event.setCanceled(true);
						}
					}
				}
			}
		}
	}
}
