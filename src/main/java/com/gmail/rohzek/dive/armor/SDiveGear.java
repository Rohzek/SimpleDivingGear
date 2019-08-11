package com.gmail.rohzek.dive.armor;

import java.util.List;
import java.util.Map;

import com.gmail.rohzek.dive.main.Main;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SDiveGear extends ItemArmor
{
	float oldFlySpeed = -1f, newFlySpeed = 0.03f;
	
	public SDiveGear(String name, ArmorMaterial mat, int renderIndex, EntityEquipmentSlot equipSlot) 
	{
		super(mat, renderIndex, equipSlot);
		this.setMaxStackSize(1);
		this.setCreativeTab(Main.S_TAB);
		this.setMaxDamage(ConfigurationManager.timeToBreathe);
		
		setNames(name);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) 
	{	
		if(!player.isCreative() && !player.isSpectator()) 
		{
			Block above = world.getBlockState(new BlockPos(player.getPosition().getX(), player.getPosition().getY() + 1, player.getPosition().getZ())).getBlock();
			
			NonNullList<ItemStack> armorSlots = player.inventory.armorInventory;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if(player.isInWater()) 
			{
				addChanges(world, player, head, chest, legs, feet, above);
				
				if(above == Blocks.WATER) // Just standing in water shouldn't use air, only being underwater
				{
					// Only damage the tank if we're consuming air, which we can only do with a helmet and the chest piece
					if((head.getItem() == SArmor.DIVE_HELMET || head.getItem() == SArmor.DIVE_HELMET_LIGHTS) && chest.getItem() == SArmor.DIVE_CHEST) 
					{
						damageTank(chest, player);
					}
				}
				else // If your head is above water, then you should still get air back
				{
					repairTank(chest, player);
				}
			}
			
			else 
			{
				removeChanges(world, player, head, chest, legs, feet);
				
				repairTank(chest, player);
			}
		}
	}
	
	private void addChanges(World world, EntityPlayer player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet, Block above) 
	{
		
		
		// If just headlamp helmet, add night vision
		if(head != null && head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS) && above == Blocks.WATER) 
		{
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 2, 0, false, false));
		}
		
		// If either helmet is on, grant respiration
		if(head != null && (head.getItem().equals(SArmor.DIVE_HELMET) || head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS)))
		{
			if(EnchantmentHelper.getEnchantments(head).get(Enchantments.RESPIRATION) == null)
			{
				head.addEnchantment(Enchantments.RESPIRATION, 1);
			}
		}
		
		// If the chest is on, grant aqua affinity
		if(chest != null && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{
			if(EnchantmentHelper.getEnchantments(chest).get(Enchantments.AQUA_AFFINITY) == null)
			{
				chest.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
			}
		}
		
		// If either helmet, and the chest is on, and you're underwater, grant water breathing
		if(head != null && (head.getItem().equals(SArmor.DIVE_HELMET) || 
		   head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS)) && 
		   chest != null && chest.getItem().equals(SArmor.DIVE_CHEST) && above == Blocks.WATER &&
		   (chest.getItemDamage() < (chest.getMaxDamage() - 40))) 
		{
			player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 2, 0, false, false));
		}
		
		// If boots are on, grant depth strider
		if(feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS)) 
		{
			if(EnchantmentHelper.getEnchantments(feet).get(Enchantments.DEPTH_STRIDER) == null)
			{
				feet.addEnchantment(Enchantments.DEPTH_STRIDER, 1);
			}
		}
		
		// If the boots and pants are on, grant easy movement through flying
		if(legs != null && legs.getItem().equals(SArmor.DIVE_LEGS) && 
		   feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS))
		{
			if(oldFlySpeed == -1f)
			{
				oldFlySpeed = player.capabilities.getFlySpeed();
			}
			
			if(world.isRemote) 
			{
				player.capabilities.setFlySpeed(newFlySpeed);
			}
			
			player.capabilities.isFlying = true;
		}
	}
	
	private void removeChanges(World world, EntityPlayer player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) 
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
			player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 2, 0, false, false));
			player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 2, 0, false, false));
			
			if(world.isRemote) 
			{
				player.capabilities.setFlySpeed(oldFlySpeed);
			}
			
			if(!player.isSpectator() && !player.isCreative()) 
			{
				player.capabilities.isFlying = false;
			}
		}
	}
	
	private void repairTank(ItemStack chest, EntityPlayer player) 
	{
		if(ConfigurationManager.consumeAir && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{
			// Convert minutes to seconds, and seconds to ms, since the world ticks in ms
			int airDuration = ((ConfigurationManager.timeToBreathe * 60) * 1000);
			
			// If the current duration isn't set correctly, then set it
			if(chest.getItem().getMaxDamage(chest) != airDuration) 
			{
				chest.getItem().setMaxDamage(airDuration);
			}
			
			// Refill with air twice as fast as it loses it (E.G. If you get 1 full minute of air, it takes 30 full seconds to refill)
			if(chest.getItemDamage() < chest.getMaxDamage()) 
			{
				chest.damageItem(-40, player);
			}
		}
	}
	
	private void damageTank(ItemStack chest, EntityPlayer player) 
	{
		if(ConfigurationManager.consumeAir && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{
			// Convert minutes to seconds, and seconds to ms, since the world ticks in ms
			int airDuration = ((ConfigurationManager.timeToBreathe * 60) * 1000);
			
			// If the current duration isn't set correctly, then set it
			if(chest.getItem().getMaxDamage(chest) != airDuration) 
			{
				chest.getItem().setMaxDamage(airDuration);
			}
			
			// Don't damage below '1', so we don't break the item
			if(chest.getItemDamage() < (chest.getMaxDamage() - 21)) 
			{
				chest.damageItem(20, player);
			}
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) 
	{
		EntityPlayer player = (EntityPlayer) entity;
		Block above = world.getBlockState(new BlockPos(player.getPosition().getX(), player.getPosition().getY() + 1, player.getPosition().getZ())).getBlock();
		removeEnchantments(stack);
		
		if(!player.isInWater()) // If you're not in water, then get air back
		{
			repairTank(stack, player);
		}
		else if(player.isInWater() && above != Blocks.WATER) // If you're in water but not not underwater, get air back
		{
			repairTank(stack, player);
		}
		
		// Remove the ability to still fly, if the armor is removed underwater
		if(!player.isCreative() && !player.isSpectator() && player.capabilities.isFlying) 
		{
			NonNullList<ItemStack> armorSlots = player.inventory.armorInventory;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if(legs != null && !legs.getItem().equals(SArmor.DIVE_LEGS) ||
			   feet != null && !feet.getItem().equals(SArmor.DIVE_BOOTS)) 
			{
				if(world.isRemote) 
				{
					player.capabilities.setFlySpeed(oldFlySpeed);
				}
				
				if(!player.isSpectator() && !player.isCreative()) 
				{
					player.capabilities.isFlying = false;
				}
			}
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem item) 
	{
		removeEnchantments(item.getItem());
		return false;
	}
	
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
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) 
	{
		if(stack.getItem() == SArmor.DIVE_CHEST) 
        {
        	if(ConfigurationManager.consumeAir) 
            {
            	long miliseconds = stack.getMaxDamage() - stack.getItemDamage();
            	long minutes = (miliseconds / 1000) / 60;
            	long seconds = (miliseconds / 1000) % 60;
                
            	if(minutes == 0 && seconds == 0 && stack.getItemDamage() == stack.getMaxDamage() - 20) 
            	{
            		tooltip.add("Air Tank Empty");
            	}
            	else
            	{
            		tooltip.add("Air Left: " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds));
            	}
            }
        }
	}
	
	@Override
	public boolean isRepairable() 
	{
		return false;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) 
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) 
	{
		return false;
	}
	
	void setNames(String name) 
	{
		setUnlocalizedName(name);
		setRegistryName(name);
	}

}