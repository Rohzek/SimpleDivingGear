package com.gmail.rohzek.dive.armor;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.main.Main;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SDiveGear extends ArmorItem
{
	float oldFlySpeed = -1f, newFlySpeed = 0.03f;
	
	public SDiveGear(String name, IArmorMaterial mat, EquipmentSlotType equipSlot) 
	{
		super(mat, equipSlot, new Item.Properties().tab(Main.DIVE_GEAR_TAB).stacksTo(1));
		setNames(name);
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
	
	void setNames(String name) 
	{
		setRegistryName(Reference.MODID, name);
	}
	
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) 
	{
		repairArmor(player.inventory.armor);
		
		if(!player.isCreative() && !player.isSpectator()) 
		{
			Block above = world.getBlockState(new BlockPos(player.getX(), player.getY() + 1, player.getZ())).getBlock();
			
			NonNullList<ItemStack> armorSlots = player.inventory.armor;
			
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
					if((head.getItem() == SArmor.DIVE_HELMET || head.getItem() == SArmor.DIVE_HELMET_LIGHTS) && chest.getItem() == SArmor.DIVE_CHEST) 
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
			
			NonNullList<ItemStack> armorSlots = player.inventory.armor;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  feet = armorSlots.get(0);
			
			// If just headlamp helmet, add night vision
			if(head != null && head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS) && above == Blocks.WATER || above == Blocks.SEAGRASS || above == Blocks.TALL_SEAGRASS || above == Blocks.KELP || above == Blocks.KELP_PLANT) 
			{
				player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 2, 0, false, false));
			}
			
			// If the chest is on, grant aqua affinity
			if(chest != null && chest.getItem().equals(SArmor.DIVE_CHEST)) 
			{
				if(EnchantmentHelper.getEnchantments(chest).get(Enchantments.AQUA_AFFINITY) == null)
				{
					chest.enchant(Enchantments.AQUA_AFFINITY, 1);
				}
			}
			
			// If boots are on, grant depth strider
			if(feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS)) 
			{
				if(EnchantmentHelper.getEnchantments(feet).get(Enchantments.DEPTH_STRIDER) == null)
				{
					feet.enchant(Enchantments.DEPTH_STRIDER, 1);
				}
			}
		}
	}
	
	public void addChanges(World world, PlayerEntity player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet, Block above) 
	{
		// If just headlamp helmet, add night vision
		if(head != null && head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS) && above == Blocks.WATER || above == Blocks.SEAGRASS || above == Blocks.TALL_SEAGRASS || above == Blocks.KELP || above == Blocks.KELP_PLANT) 
		{
			player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 2, 0, false, false));
		}
		
		// If either helmet is on, grant respiration
		if(head != null && (head.getItem().equals(SArmor.DIVE_HELMET) || head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS)))
		{
			if(EnchantmentHelper.getEnchantments(head).get(Enchantments.RESPIRATION) == null)
			{
				head.enchant(Enchantments.RESPIRATION, 1);
			}
		}
		
		// If the chest is on, grant aqua affinity
		if(chest != null && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{
			if(EnchantmentHelper.getEnchantments(chest).get(Enchantments.AQUA_AFFINITY) == null)
			{
				chest.enchant(Enchantments.AQUA_AFFINITY, 1);
			}
		}
		
		// If either helmet, and the chest is on, and you're underwater, grant water breathing
		if(head != null && (head.getItem().equals(SArmor.DIVE_HELMET) || 
		   head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS)) && 
		   chest != null && chest.getItem().equals(SArmor.DIVE_CHEST) && above == Blocks.WATER || above == Blocks.SEAGRASS || above == Blocks.TALL_SEAGRASS || above == Blocks.KELP || above == Blocks.KELP_PLANT &&
		   (chest.getDamageValue() < (chest.getMaxDamage() - 40))) 
		{
			if(chest.getDamageValue() < (chest.getMaxDamage() - 40))
			{
				player.addEffect(new EffectInstance(Effects.WATER_BREATHING, 2, 0, false, false));
			}
		}
		
		// If boots are on, grant depth strider
		if(feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS)) 
		{
			if(EnchantmentHelper.getEnchantments(feet).get(Enchantments.DEPTH_STRIDER) == null)
			{
				feet.enchant(Enchantments.DEPTH_STRIDER, 1);
			}
		}
		
		// If the boots and pants are on, grant easy movement through 'flying'
		if(legs != null && legs.getItem().equals(SArmor.DIVE_LEGS) && 
		   feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS))
		{
			if(oldFlySpeed == -1f)
			{
				oldFlySpeed = player.abilities.getFlyingSpeed();
			}
			
			if(world.isClientSide) 
			{
				player.abilities.setFlyingSpeed(newFlySpeed);
			}
			
			player.abilities.flying = true;
		}
	}
	
	public void removeChanges(World world, PlayerEntity player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) 
	{
		if(head != null && head.getItem().equals(SArmor.DIVE_HELMET)||
		   head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS)) 
		{
			removeEnchantments(head);
		}
				
		if(chest != null && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{
			removeEnchantments(chest);
		}
		
		if(feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS)) 
		{
			removeEnchantments(feet);
		}
		
		if(head != null && head.getItem().equals(SArmor.DIVE_HELMET) ||
		   head != null && head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS) ||
		   chest != null && chest.getItem().equals(SArmor.DIVE_CHEST) ||
		   legs != null && legs.getItem().equals(SArmor.DIVE_LEGS) || 
		   feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS))
		{
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 2, 0, false, false));
			player.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 2, 0, false, false));
			
			if(world.isClientSide) 
			{
				player.abilities.setFlyingSpeed(oldFlySpeed);
			}
			
			if(!player.isSpectator() && !player.isCreative()) 
			{
				player.abilities.flying = false;
			}
		}
	}
	
	public void repairTank(ItemStack chest, PlayerEntity player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{
			// By fdefault we refill with air twice as fast as it loses it 
			// (E.G. If you get 1 full minute of air, it takes 30 full seconds to refill)
			// But we allow up to 4 times faster
			if(chest.getDamageValue() < chest.getMaxDamage()) 
			{
				chest.setDamageValue(chest.getDamageValue() - (20 * ConfigurationManager.GENERAL.regainAirSpeed.get()));
			}
		}
	}
	
	public void damageTank(ItemStack chest, PlayerEntity player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.DIVE_CHEST)) 
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
			
			if(head != null && head.getItem().equals(SArmor.DIVE_HELMET) || 
			   head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS)) 
			{
				if(head.isDamaged()) 
				{
					head.setDamageValue(0);
				}
			}
			
			if(legs != null && legs.getItem().equals(SArmor.DIVE_LEGS)) 
			{
				if(legs.isDamaged()) 
				{
					legs.setDamageValue(0);
				}
			}
			
			if(feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS)) 
			{
				if(feet.isDamaged()) 
				{
					feet.setDamageValue(0);
				}
			}
		}
	}
	
	// Was named onUpdate in previous versions, is now inventoryTick
	@SuppressWarnings("unused")
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) 
	{
		PlayerEntity player = (PlayerEntity) entity;
		Block above = world.getBlockState(new BlockPos(player.getX(), player.getY() + 1, player.getZ())).getBlock();
		removeEnchantments(stack);
		
		repairArmor(player.inventory.armor);
		
		// If you're not in water, then get air back
		if(!player.isInWater()) 
		{
			repairTank(stack, player);
		}
		// If you're in water but not not underwater, get air back
		else if(player.isInWater() && above != Blocks.WATER)
		{
			repairTank(stack, player);
		}
		
		// Remove the ability to still fly, if the armor is removed underwater
		if(!player.isCreative() && !player.isSpectator() && player.abilities.flying) 
		{
			NonNullList<ItemStack> armorSlots = player.inventory.armor;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if(legs != null && !legs.getItem().equals(SArmor.DIVE_LEGS) ||
			   feet != null && !feet.getItem().equals(SArmor.DIVE_BOOTS)) 
			{
				if(world.isClientSide) 
				{
					player.abilities.setFlyingSpeed(oldFlySpeed);
				}
				
				if(!player.isSpectator() && !player.isCreative()) 
				{
					player.abilities.flying = false;
				}
			}
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) 
	{
		removeEnchantments(entity.getItem());
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removeEnchantments(ItemStack stack) 
	{
		Map enchants = EnchantmentHelper.getEnchantments(stack);
		
		if(stack != null) 
		{
			if(enchants.get(Enchantments.RESPIRATION) != null)
			{
				enchants.remove(Enchantments.RESPIRATION);
				EnchantmentHelper.setEnchantments(enchants, stack);
			}
			
			if(enchants.get(Enchantments.AQUA_AFFINITY) != null)
			{
				enchants.remove(Enchantments.AQUA_AFFINITY);
				EnchantmentHelper.setEnchantments(enchants, stack);
			}
			
			if(enchants.get(Enchantments.DEPTH_STRIDER) != null)
			{
				enchants.remove(Enchantments.DEPTH_STRIDER);
				EnchantmentHelper.setEnchantments(enchants, stack);
			}
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
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) 
	{
		// Have to return the exact path to the armor, just passing standard resource location won't work
		return Reference.RESOURCEID + "textures/models/armor/divegear" + (slot == EquipmentSlotType.LEGS ? "_layer_2" : "_layer_1") + ".png";
	}
	
	@Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if(stack.getItem() == SArmor.DIVE_CHEST) 
        {
        	long miliseconds = stack.getMaxDamage() - stack.getDamageValue();
        	
        	long minutes = (miliseconds / 1000) / 60;
        	long seconds = (miliseconds / 1000) % 60;
            
        	if(minutes == 0 && seconds == 0 && stack.getDamageValue() == stack.getMaxDamage() - 20) 
        	{
        		tooltip.add(new StringTextComponent("Air Tank Empty"));
        	}
        	else
        	{
        		tooltip.add(new StringTextComponent("Air Left: " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds)));
        	}
        }
    }
}