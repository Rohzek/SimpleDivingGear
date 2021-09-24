package com.gmail.rohzek.dive.events;

import com.gmail.rohzek.dive.armor.SArmor;
import com.gmail.rohzek.dive.util.LogHelper;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class RemoveLavaView 
{
	@SubscribeEvent
	public static void removeLavaView(EntityViewRenderEvent.FogDensity event) 
	{
		Entity entity = event.getInfo().getEntity();
		
		if(entity instanceof LivingEntity) 
		{
			LivingEntity lEntity = (LivingEntity) entity;
			
			if(lEntity instanceof Player) 
			{
				Player player = (Player) lEntity;
				
				if(player.isInLava())
				{
					Block above = player.level.getBlockState(new BlockPos(player.getX(), player.getY() + 2, player.getZ())).getBlock();

					if(above == Blocks.LAVA) 
					{
						NonNullList<ItemStack> armorSlots = player.getInventory().armor;
						
						ItemStack head = armorSlots.get(3);
						
						if(head.getItem().equals(SArmor.NETHER_DIVE_HELMET) || head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS)) 
						{
							LogHelper.debug("Remove that lava view!");
							
							RenderSystem.setShaderFogStart(0.0F);
							RenderSystem.setShaderFogEnd(1024F);
					        event.setDensity(0.05F);
					        event.setCanceled(true);
						}
					}
				}
			}
		}
	}
}
