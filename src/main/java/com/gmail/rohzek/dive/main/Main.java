package com.gmail.rohzek.dive.main;

import com.gmail.rohzek.dive.armor.SArmor;
import com.gmail.rohzek.dive.items.DiveGearItems;
import com.gmail.rohzek.dive.lib.DeferredRegistration;
import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;
import com.gmail.rohzek.dive.util.TimeOutput;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MODID)
public class Main 
{
	public Main() 
	{
		// Register the mod
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.register(this);
		
		// Start deferred registration
		DeferredRegistration.register(bus);
		DiveGearItems.register();
		SArmor.register();
		
		// Register configuration file
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigurationManager.spec);
	}
	
	@SubscribeEvent
	public void setup(FMLCommonSetupEvent event) 
	{
		LogHelper.log("Hello Minecraft, how are you? Did you know that Tony loves Amy? " + TimeOutput.getTimeTogether());
	}
	
	@SubscribeEvent
	public static void removeLavaView(ViewportEvent.RenderFog event)
	{
		//Entity entity = event.getInfo().getEntity();
		Entity entity = event.getRenderer().getMainCamera().getEntity();

		if (entity instanceof LivingEntity) 
		{
			LivingEntity lEntity = (LivingEntity) entity;

			if (lEntity instanceof Player)
			{
				Player player = (Player) lEntity;

				if (player.isInLava()) 
				{
					Block above = player.level().getBlockState(new BlockPos((int)player.getX(), (int)player.getY() + 2, (int)player.getZ())).getBlock();

					if (above == Blocks.LAVA) 
					{
						NonNullList<ItemStack> armorSlots = player.getInventory().armor;

						ItemStack head = armorSlots.get(3);

						if (head.getItem().equals(SArmor.NETHER_DIVE_HELMET.get()) || head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS.get())) 
						{
							LogHelper.log("Remove that lava view!");

							// See if we can fine tune by going into the nether
							RenderSystem.setShaderFogStart(0.0F);
							RenderSystem.setShaderFogEnd(80F);
						}
					}
				}
			}
		}
	}
}
