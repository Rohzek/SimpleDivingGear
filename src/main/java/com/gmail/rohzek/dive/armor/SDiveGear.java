package com.gmail.rohzek.dive.armor;

import java.util.List;

import javax.annotation.Nullable;

import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.main.Main;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SDiveGear extends ArmorItem
{
	float oldFlySpeed = -1f, newFlySpeed = 0.03f;
	
	public SDiveGear(ArmorMaterial mat, EquipmentSlot equipSlot) 
	{
		super(mat, equipSlot, new Item.Properties().stacksTo(1));
	}
		
	@Override
	public boolean isDamageable(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) 
	{
		return ((ConfigurationManager.GENERAL.minutesOfAir.get() * 60) * 1000);
	}
	
	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) 
	{
		repairArmor(player.getInventory().armor);
		
		if(!player.isCreative() && !player.isSpectator()) 
		{
			Block above = world.getBlockState(new BlockPos(player.getX(), player.getY() + 1, player.getZ())).getBlock();
			
			NonNullList<ItemStack> armorSlots = player.getInventory().armor;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if(player.isInWater()) 
			{
				LogHelper.debug("I'm in water!");
				addChanges(world, player, head, chest, legs, feet, above);
				
				// Just standing in water shouldn't use air, only being underwater
				if(above == Blocks.WATER || above == Blocks.SEAGRASS || above == Blocks.TALL_SEAGRASS || above == Blocks.KELP || above == Blocks.KELP_PLANT)
				{
					// Only damage the tank if we're consuming air, which we can only do with a helmet and the chest piece
					if((head.getItem() == SArmor.DIVE_HELMET.get().asItem() || head.getItem() == SArmor.DIVE_HELMET_LIGHTS.get().asItem()) && chest.getItem() == SArmor.DIVE_CHEST.get().asItem()) 
					{
						LogHelper.debug("I'm underwater, damage the air tank!");
						damageTank(chest, player);
					}
				}
				else // If your head is above water, then you should still get air back
				{
					LogHelper.debug("I'm above water, repair the air tank!");
					repairTank(chest, player);
				}
			}
			
			else 
			{
				LogHelper.debug("I'm not in water, repair the air tank, and remove the buffs!");
				removeChanges(world, player, head, chest, legs, feet);
				repairTank(chest, player);
			}
		}
		
		else if (player.isCreative() || player.isSpectator() && player.isInWater())
		{
			// Even in Creative mode
			Block above = world.getBlockState(new BlockPos(player.getX(), player.getY() + 1, player.getZ())).getBlock();
			
			NonNullList<ItemStack> armorSlots = player.getInventory().armor;
			
			ItemStack head = armorSlots.get(3);
			
			// If just headlamp helmet, add night vision
			if(head != null && head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS.get().asItem()) && above == Blocks.WATER || above == Blocks.SEAGRASS || above == Blocks.TALL_SEAGRASS || above == Blocks.KELP || above == Blocks.KELP_PLANT) 
			{
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2, 0, false, false));
			}
		}
	}
	
	public void addChanges(Level world, Player player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet, Block above) 
	{
		// If just headlamp helmet, add night vision
		if(head != null && head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS.get().asItem()) && above == Blocks.WATER || above == Blocks.SEAGRASS || above == Blocks.TALL_SEAGRASS || above == Blocks.KELP || above == Blocks.KELP_PLANT) 
		{
			player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2, 0, false, false));
			
		}
		
		// If either helmet, and the chest is on, and you're underwater, grant water breathing
		if(head != null && (head.getItem().equals(SArmor.DIVE_HELMET.get().asItem()) || 
		   head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS.get().asItem())) && 
		   chest != null && chest.getItem().equals(SArmor.DIVE_CHEST.get().asItem()) && above == Blocks.WATER || above == Blocks.SEAGRASS || above == Blocks.TALL_SEAGRASS || above == Blocks.KELP || above == Blocks.KELP_PLANT &&
		   (chest.getDamageValue() < (chest.getMaxDamage() - 40))) 
		{
			if(chest.getDamageValue() < (chest.getMaxDamage() - 40))
			{
				player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 2, 0, false, false));
			}
		}
		
		// If the boots and pants are on, grant easy movement through 'flying'
		if(legs != null && legs.getItem().equals(SArmor.DIVE_LEGS.get().asItem()) && 
		   feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS.get().asItem()))
		{
			if(oldFlySpeed == -1f)
			{
				oldFlySpeed = player.getAbilities().getFlyingSpeed();
			}
			
			if(world.isClientSide) 
			{
				player.getAbilities().setFlyingSpeed(newFlySpeed);
			}
			
			player.getAbilities().flying = true;
		}
	}
	
	public void removeChanges(Level world, Player player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) 
	{		
		if(head != null && head.getItem().equals(SArmor.DIVE_HELMET.get().asItem()) ||
		   head != null && head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS.get().asItem()) ||
		   chest != null && chest.getItem().equals(SArmor.DIVE_CHEST.get().asItem()) ||
		   legs != null && legs.getItem().equals(SArmor.DIVE_LEGS.get().asItem()) || 
		   feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS.get().asItem()))
		{
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 0, false, false));
			player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 2, 0, false, false));
			
			if(world.isClientSide) 
			{
				player.getAbilities().setFlyingSpeed(oldFlySpeed);
			}
			
			if(!player.isSpectator() && !player.isCreative()) 
			{
				player.getAbilities().flying = false;
			}
		}
	}
	
	public void repairTank(ItemStack chest, Player player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.DIVE_CHEST.get().asItem())) 
		{
			// By default we refill with air twice as fast as it loses it 
			// (E.G. If you get 1 full minute of air, it takes 30 full seconds to refill)
			// But we allow up to 4 times faster
			if(chest.getDamageValue() < chest.getMaxDamage()) 
			{
				chest.setDamageValue(chest.getDamageValue() - (20 * ConfigurationManager.GENERAL.regainAirSpeed.get()));
			}
		}
	}
	
	public void damageTank(ItemStack chest, Player player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.DIVE_CHEST.get().asItem())) 
		{
			// We don't want to break the item, so only lower if we still have room to lower
			if(chest.getDamageValue() < (chest.getMaxDamage() - 21)) 
			{
				LogHelper.debug("I should be damaging the tank right now. Damage is at: " + chest.getDamageValue());
				chest.setDamageValue((chest.getDamageValue() + 20));
				
				// If air tank is equipped underwater, after they've been breathing for awhile, refill the air instantly.
				player.setAirSupply(player.getMaxAirSupply());
			}
		}
	}
	
	public void repairArmor(NonNullList<ItemStack> armorSlots) 
	{
		if(ConfigurationManager.GENERAL.invincibleArmor.get()) 
		{
			ItemStack head = armorSlots.get(3),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if(head != null && head.getItem().equals(SArmor.DIVE_HELMET.get().asItem()) || 
			   head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS.get().asItem())) 
			{
				if(head.isDamaged()) 
				{
					head.setDamageValue(0);
				}
			}
			
			if(legs != null && legs.getItem().equals(SArmor.DIVE_LEGS.get().asItem())) 
			{
				if(legs.isDamaged()) 
				{
					legs.setDamageValue(0);
				}
			}
			
			if(feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS.get().asItem())) 
			{
				if(feet.isDamaged()) 
				{
					feet.setDamageValue(0);
				}
			}
		}
	}
	
	// Was named onUpdate in previous versions, is now inventoryTick
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) 
	{
		Player player = (Player) entity;
		Block above = world.getBlockState(new BlockPos(player.getX(), player.getY() + 1, player.getZ())).getBlock();
		
		repairArmor(player.getInventory().armor);
		
		// If you're not in water, then get air back
		if(!player.isInWater()) 
		{
			repairTank(stack, player);
		}
		// If you're in water but not not underwater, get air back
		else if(player.isInWater() && above != Blocks.WATER)
		{
			repairTank(stack, player);
			
			// This should theoretically fix issue #25, at least outside of water
			// Remove the ability to still fly, if the armor is removed underwater
			if(!player.isCreative() && !player.isSpectator() && player.getAbilities().flying) 
			{
				NonNullList<ItemStack> armorSlots = player.getInventory().armor;
				
				ItemStack legs = armorSlots.get(1),
						  feet = armorSlots.get(0);
				
				if(legs != null && !legs.getItem().equals(SArmor.DIVE_LEGS.get().asItem()) ||
				   feet != null && !feet.getItem().equals(SArmor.DIVE_BOOTS.get().asItem())) 
				{
					if(world.isClientSide) 
					{
						player.getAbilities().setFlyingSpeed(oldFlySpeed);
					}
					
					if(!player.isSpectator() && !player.isCreative()) 
					{
						player.getAbilities().flying = false;
					}
				}
			}
		}
	}
	
	@Override
	public void onCraftedBy(ItemStack item, Level level, Player player) 
	{
		// Add the enchantments here instead of only when armor is worn
		if(item.getItem().equals(SArmor.DIVE_HELMET.get().asItem()) || item.getItem().equals(SArmor.DIVE_HELMET_LIGHTS.get().asItem())) 
		{
			item.enchant(Enchantments.RESPIRATION, 1);
		}
		
		if(item.getItem().equals(SArmor.DIVE_CHEST.get().asItem())) 
		{
			item.enchant(Enchantments.AQUA_AFFINITY, 1);
		}
		
		if(item.getItem().equals(SArmor.DIVE_LEGS.get().asItem())) 
		{
			item.enchant(Enchantments.SWIFT_SNEAK, 1);
		}
		
		if(item.getItem().equals(SArmor.DIVE_BOOTS.get().asItem())) 
		{
			item.enchant(Enchantments.DEPTH_STRIDER, 1);
		}
	}
	
	@Override
	public boolean isRepairable(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) 
	{
		return false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) 
	{
		if(tab.getId() == Main.DIVE_GEAR_TAB.getId()) 
		{
			ItemStack stack = new ItemStack(this);
			
			if(stack.getItem().equals(SArmor.DIVE_HELMET.get().asItem()) || stack.getItem().equals(SArmor.DIVE_HELMET_LIGHTS.get().asItem())) 
			{
				stack.enchant(Enchantments.RESPIRATION, 1);
			}
			
			if(stack.getItem().equals(SArmor.DIVE_CHEST.get().asItem())) 
			{
				stack.enchant(Enchantments.AQUA_AFFINITY, 1);
			}
			
			if(stack.getItem().equals(SArmor.DIVE_LEGS.get().asItem())) 
			{
				stack.enchant(Enchantments.SWIFT_SNEAK, 1);
			}
			
			if(stack.getItem().equals(SArmor.DIVE_BOOTS.get().asItem())) 
			{
				stack.enchant(Enchantments.DEPTH_STRIDER, 1);
			}
			
			items.add(stack);

			super.fillItemCategory(tab, items);
		}
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) 
	{
		return false;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) 
	{
		return false;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) 
	{
		return Reference.RESOURCEID + "textures/models/armor/divegear" + (slot == EquipmentSlot.LEGS ? "_layer_2" : "_layer_1") + ".png";
	}
	
	@Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if(stack.getItem() == SArmor.DIVE_CHEST.get().asItem()) 
        {
        	long miliseconds = stack.getMaxDamage() - stack.getDamageValue();
        	
        	long minutes = (miliseconds / 1000) / 60;
        	long seconds = (miliseconds / 1000) % 60;
            
        	if(minutes == 0 && seconds == 0 && stack.getDamageValue() == stack.getMaxDamage() - 20) 
        	{
        		tooltip.add(Component.translatable("display.simpledivegear.airempty"));
        	}
        	else
        	{
        		tooltip.add(Component.translatable(I18n.get("display.simpledivegear.airleft") + ": " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds)));
        	}
        }
    }
}