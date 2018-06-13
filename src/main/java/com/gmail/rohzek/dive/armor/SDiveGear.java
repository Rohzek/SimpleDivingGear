package com.gmail.rohzek.dive.armor;

import java.util.Map;

import com.gmail.rohzek.dive.main.Main;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;

import net.minecraft.block.Block;
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
		if(!player.capabilities.isCreativeMode) 
		{
			NonNullList<ItemStack> armorSlots = player.inventory.armorInventory;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if(player.isInWater()) 
			{
				Block above = world.getBlockState(new BlockPos(player.getPosition().getX(), player.getPosition().getY() + 1, player.getPosition().getZ())).getBlock();
				
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
				   head.getItem().equals(SArmor.DIVE_HELMET_LIGHTS)) && chest != null && chest.getItem().equals(SArmor.DIVE_CHEST) && above == Blocks.WATER) 
				{
					player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 2, 0, false, false));
					
					/*
					// Damage tank
					if(ConfigurationManager.consumeAir) 
					{
						long current = loadCurrent(chest);
						
						// If has never been used, initialize it.
						if(loadLastCheck(chest) == 0)
						{
							// Convert minutes to ms
							long time = ((ConfigurationManager.timeToBreathe * 60) * 1000);
							saveLastCheck(chest, time);
						}
						
						if(current < 60000) 
						{
							current++;
							saveCurrent(chest, current);
						}
						else
						{
							saveCurrent(chest, 0);
							
							long timer = loadLastCheck(chest) - 59999;
							
							LogHelper.log("Time left minute: " + timer / 1000 + " seconds: " + (timer / 1000) / 60);
							
							if(timer < 1000) 
							{
								chest.damageItem(chest.getMaxDamage(), player); // Break it
							}
							
							else
							{
								// Remove 1 minute from time
								saveLastCheck(chest, timer);
								chest.damageItem(1, player);
							}
						}
					}
					*/
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
					if(!player.capabilities.isCreativeMode && !player.capabilities.isFlying) 
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
			}
			
			// If the player isn't in water, and is wearing any of the armor, remove all benefits and apply slow and mining fatigue
			else 
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
					
					if(!player.capabilities.isCreativeMode && player.capabilities.isFlying) 
					{
						if(world.isRemote) 
						{
							player.capabilities.setFlySpeed(oldFlySpeed);
						}
						
						player.capabilities.isFlying = false;
					}
				}
			}
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		removeEnchantments(stack);
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
	
	public long loadLastCheck(ItemStack stack)
	{
		NBTTagCompound data = stack.getTagCompound();
		long time = 0;
		
		if(data.hasKey("lastTime"))
		{
			time = data.getLong("lastTime");
		}
		
		return time;
	}
	
	public void saveLastCheck(ItemStack stack, long time) 
	{
		NBTTagCompound data = stack.getTagCompound();
		
		if(data == null)
		{
			data = new NBTTagCompound();
		}
		
		data.setLong("lastTime", time);
	}
	
	public long loadCurrent(ItemStack stack)
	{
		NBTTagCompound data = stack.getTagCompound();
		long time = 0;
		
		if(data.hasKey("currentCheck"))
		{
			time = data.getLong("currentCheck");
		}
		
		return time;
	}
	
	public void saveCurrent(ItemStack stack, long time) 
	{
		NBTTagCompound data = stack.getTagCompound();
		
		if(data == null)
		{
			data = new NBTTagCompound();
		}
		
		data.setLong("currentCheck", time);
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
