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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
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
	
	// Registered on the MOD event bus
	@SubscribeEvent
	public void buildContents(CreativeModeTabEvent.Register event) 
	{
		ItemStack diveHelmet = new ItemStack(SArmor.DIVE_HELMET.get());
		diveHelmet.enchant(Enchantments.RESPIRATION, 1);
		ItemStack diveHelmetLight = new ItemStack(SArmor.DIVE_HELMET_LIGHTS.get());
		diveHelmetLight.enchant(Enchantments.RESPIRATION, 1);
		ItemStack diveChest = new ItemStack(SArmor.DIVE_CHEST.get());
		diveChest.enchant(Enchantments.AQUA_AFFINITY, 1);
		ItemStack diveLegs = new ItemStack(SArmor.DIVE_LEGS.get());
		diveLegs.enchant(Enchantments.SWIFT_SNEAK, 1);
		ItemStack diveBoots = new ItemStack(SArmor.DIVE_BOOTS.get());
		diveBoots.enchant(Enchantments.DEPTH_STRIDER, 1);
		
		ItemStack diveHelmetNether = new ItemStack(SArmor.NETHER_DIVE_HELMET.get());
		diveHelmetNether.enchant(Enchantments.FIRE_PROTECTION, 1);
		ItemStack diveHelmetLightNether = new ItemStack(SArmor.NETHER_DIVE_HELMET_LIGHTS.get());
		diveHelmetLightNether.enchant(Enchantments.FIRE_PROTECTION, 1);
		ItemStack diveChestNether = new ItemStack(SArmor.NETHER_DIVE_CHEST.get());
		diveChestNether.enchant(Enchantments.FIRE_PROTECTION, 1);
		ItemStack diveLegsNether = new ItemStack(SArmor.NETHER_DIVE_LEGS.get());
		diveLegsNether.enchant(Enchantments.FIRE_PROTECTION, 1);
		ItemStack diveBootsNether = new ItemStack(SArmor.NETHER_DIVE_BOOTS.get());
		diveBootsNether.enchant(Enchantments.FIRE_PROTECTION, 1);
		
		event.registerCreativeModeTab(new ResourceLocation(Reference.MODID, "divegeartab"), builder ->
		// Set name of tab to display
		builder.title(Component.translatable("itemgroup." + Reference.MODID + ".divegeartab"))
		// Set icon of creative tab
		.icon(() -> new ItemStack(DiveGearItems.DIVE_HELMET_CORE.get()))
		// Add default items to tab
		.displayItems((params, output) -> 
		{
			output.accept(DiveGearItems.DIVE_HELMET_CORE.get());
			
			output.accept(diveHelmet);
			output.accept(diveHelmetLight);
			output.accept(diveChest);
			output.accept(diveLegs);
			output.accept(diveBoots);
			
			output.accept(diveHelmetNether);
			output.accept(diveHelmetLightNether);
			output.accept(diveChestNether);
			output.accept(diveLegsNether);
			output.accept(diveBootsNether);
		}));
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
					Block above = player.level .getBlockState(new BlockPos((int)player.getX(), (int)player.getY() + 2, (int)player.getZ())).getBlock();

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
