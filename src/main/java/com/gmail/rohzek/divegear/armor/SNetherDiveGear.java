package com.gmail.rohzek.divegear.armor;

import java.util.List;

import com.gmail.rohzek.divegear.lib.ConfigurationManager;
import com.gmail.rohzek.divegear.lib.LogHelper;
import com.gmail.rohzek.divegear.lib.Reference;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterial.Layer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

public class SNetherDiveGear extends ArmorItem
{
	public SNetherDiveGear(Holder<ArmorMaterial> mat, Type equipSlot) 
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
		return (((ConfigurationManager.GENERAL.minutesOfAir.get()) * 60) * 1000);
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
	public boolean isFoil(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) 
	{
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) 
	{
		return false;
	}
	
	@Override
	public @org.jetbrains.annotations.Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, Layer layer, boolean innerModel)
	{
		return ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/models/armor/netherdivegear" + (slot == EquipmentSlot.LEGS ? "_layer_2" : "_layer_1") + ".png");
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag tooltipFlag) 
	{
		if(stack.getItem() == SArmor.NETHER_DIVE_CHEST.get().asItem()) 
        {
        	long miliseconds = stack.getMaxDamage() - stack.getDamageValue();
        	
        	long minutes = (miliseconds / 1000) / 60;
        	long seconds = (miliseconds / 1000) % 60;
            
        	if(minutes == 0 && seconds == 0 && stack.getDamageValue() == stack.getMaxDamage() - 20) 
        	{
        		tooltips.add(Component.translatable("display.simpledivegear.coolantempty"));
        	}
        	else
        	{
        		tooltips.add(Component.translatable(I18n.get("display.simpledivegear.coolantleft") + ": " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds)));
        	}
        }
	}
	
	// Previously was split into onArmorTick and inventoryTick. onArmorTick seems to have been removed so we need to combine both functions here
		@Override
		public void inventoryTick(ItemStack stack, Level world, Entity entity, int slotId, boolean isSelected) 
		{
			Player player = (Player)entity;
			NonNullList<ItemStack> armor = player.getInventory().armor;
			ItemStack head = armor.get(3),
					  chest = armor.get(2);
			
			if(player != null)
			{
				// If armor is set to be invincible, we should keep armor repaired
				repairArmor(player);
				
				// We don't care about the player's statuses if not in survival
				if(!player.isCreative() && !player.isSpectator()) 
				{
					if(isInLava(player)) 
					{
						addChanges(world, player);
						
						if(isUnderLava(player)) 
						{
							// If a helmet and the chest is equipped, breathe air from tank
							if(isEquipped(head, player) && isEquipped(chest, player)) 
							{
								damageTank(chest, player);
							}
						}
						else 
						{
							repairTank(chest, player);
						}
					}
					else 
					{
						repairTank(chest, player);
						removeChanges(world, player);
					}
				}
			}
		}
		
		/**
		 * TODO: Test & Fix
		 * This may or may not work
		 */
		@Override
		public void onCraftedBy(ItemStack item, Level level, Player player)
		{
			/*
			// Add the enchantments here instead of only when armor is worn
			if(item.getItem().equals(SArmor.NETHER_DIVE_HELMET.get().asItem()) || item.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS.get().asItem())) 
			{
				item.enchant((Holder<Enchantment>)Enchantments.FIRE_PROTECTION, 1);
			}
					
			if(item.getItem().equals(SArmor.NETHER_DIVE_CHEST.get().asItem())) 
			{
				item.enchant((Holder<Enchantment>)Enchantments.FIRE_PROTECTION, 1);
			}
					
			if(item.getItem().equals(SArmor.NETHER_DIVE_LEGS.get().asItem())) 
			{
				item.enchant((Holder<Enchantment>)Enchantments.FIRE_PROTECTION, 1);
			}
					
			if(item.getItem().equals(SArmor.NETHER_DIVE_BOOTS.get().asItem())) 
			{
				item.enchant((Holder<Enchantment>)Enchantments.FIRE_PROTECTION, 1);
			}
			*/
		}
		
		/**
		 * TODO: Mayfly is used here
		 * @param world
		 * @param player
		 */
		@SuppressWarnings("deprecation")
		void addFlyingAbility(Level world, Player player) 
		{
			if(!player.isSpectator() && !player.isCreative()) 
			{		
				player.getAbilities().flying = true;
				player.getAbilities().mayfly = true;
			}
		}
		
		/**
		 * TODO: Mayfly is used here
		 * @param world
		 * @param player
		 */
		@SuppressWarnings("deprecation")
		void removeFlyingAbility(Level world, Player player) 
		{
			if(!player.isSpectator() && !player.isCreative()) 
			{		
				player.getAbilities().flying = false;
				player.getAbilities().mayfly = false;
			}
		}
			
		public void addChanges(Level world, Player player) 
		{
			NonNullList<ItemStack> armorSlots = player.getInventory().armor;
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			// Add night vision if lighted helmet is worn under lava
			if(head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS.get()) && isEquipped(head, player) && isUnderLava(player)) 
			{
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2, 0, false, false));
			}
			
			if(isEquipped(head, player) && isEquipped(chest, player) && (chest.getDamageValue() < (chest.getMaxDamage() - 40)) && isInLava(player))
			{
				player.extinguishFire();
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 0, false, false));
			}
			
			if(isEquipped(legs, player) && isEquipped(feet, player)) 
			{
				addFlyingAbility(world, player);
			}
			else
			{
				removeFlyingAbility(world, player);
			}
		}
			
		public void removeChanges(Level world, Player player) 
		{
			NonNullList<ItemStack> armorSlots = player.getInventory().armor;
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if((isEquipped(head, player) || isEquipped(chest, player) || isEquipped(legs, player) || isEquipped(feet, player)) && !isInLava(player))
			{
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 0, false, false));
			}
			if(isEquipped(chest, player)) 
			{
				player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 2, 0, false, false));
			}
			
			removeFlyingAbility(world, player);
			
			player.extinguishFire();
		}
		
		public void repairTank(ItemStack chest, Player player) 
		{
			if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.NETHER_DIVE_CHEST.get().asItem())) 
			{
				if(chest.getDamageValue() < chest.getMaxDamage()) 
				{
					chest.setDamageValue(chest.getDamageValue() - (20 * ConfigurationManager.GENERAL.airReturnSpeed.get()));
				}
			}
		}
		
		public void damageTank(ItemStack chest, Player player) 
		{
			if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.NETHER_DIVE_CHEST.get().asItem()))
			{
				// We don't want to break the item, so only lower if we still have room to lower
				if(chest.getDamageValue() < (chest.getMaxDamage() - 21)) 
				{
					LogHelper.Debug("I should be damaging the tank right now. Damage is at: " + chest.getDamageValue() + "Max damage is: " + chest.getMaxDamage());
					chest.setDamageValue((chest.getDamageValue() + 20));
					
					// If air tank is equipped under lava, after they've been breathing for awhile, refill the air instantly.
					player.setAirSupply(player.getMaxAirSupply());
				}
			}
		}
		
		public void repairArmor(Player player) 
		{
			NonNullList<ItemStack> armorSlots = player.getInventory().armor;
			
			if(ConfigurationManager.GENERAL.invincibleArmor.get()) 
			{
				ItemStack head = armorSlots.get(3),
						  legs = armorSlots.get(1),
						  feet = armorSlots.get(0);
				
				if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET.get().asItem()) || 
				   head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS.get().asItem())) 
				{
					if(head.isDamaged()) 
					{
						head.setDamageValue(0);
					}
				}
				
				if(legs != null && legs.getItem().equals(SArmor.NETHER_DIVE_LEGS.get().asItem())) 
				{
					if(legs.isDamaged()) 
					{
						legs.setDamageValue(0);
					}
				}
				
				if(feet != null && feet.getItem().equals(SArmor.NETHER_DIVE_BOOTS.get().asItem())) 
				{
					if(feet.isDamaged()) 
					{
						feet.setDamageValue(0);
					}
				}
			}
		}
		
		boolean isInInventory(ItemStack stack, Player player) 
		{
			if(player != null) 
			{
				Inventory inventory = player.getInventory();
				
				if(inventory.items.contains(stack) || inventory.offhand.contains(stack) || inventory.getSelected().getItem().equals(stack.getItem())) 
				{
					return true;
				}
			}
			
			return false;
		}
		
		boolean isEquipped(ItemStack stack, Player player) 
		{
			if(player != null) 
			{
				NonNullList<ItemStack> armor = player.getInventory().armor;
				
				ItemStack head = armor.get(3),
						  chest = armor.get(2),
						  legs = armor.get(1),
						  feet = armor.get(0);
				
				if(head.getItem().equals(stack.getItem()) || 
				   chest.getItem().equals(stack.getItem()) || 
				   legs.getItem().equals(stack.getItem()) || 
				   feet.getItem().equals(stack.getItem()))
				{
					if(isDiveGear(stack)) 
					{
						return true;
					}
				}
			}
			
			return false;
		}
		
		boolean isDiveGear(ItemStack stack)
		{
			if(stack.getItem().equals(SArmor.NETHER_DIVE_HELMET.get()) ||
			   stack.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS.get()) ||
			   stack.getItem().equals(SArmor.NETHER_DIVE_CHEST.get()) ||
			   stack.getItem().equals(SArmor.NETHER_DIVE_LEGS.get()) || 
			   stack.getItem().equals(SArmor.NETHER_DIVE_BOOTS.get()) ) 
			{
				return true;
			}
			
			return false;
		}
		
		boolean isInLava(Player player) 
		{
			if(player != null) 
			{
				if(player.isInLava() && !player.isInWater()) 
				{
					LogHelper.Debug("I'm in lava!");
					return true;
				}
			}
			
			return false;
		}
		
		boolean isUnderLava(Player player) 
		{
			if(player != null) 
			{
				if(isInLava(player) && player.isEyeInFluidType(Fluids.LAVA.getFluidType())) 
				{
					LogHelper.Debug("I'm under lava!");
					return true;
				}
			}
			
			return false;
		}
}
