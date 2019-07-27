package com.gmail.rohzek.dive.armor;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.main.Main;
import com.gmail.rohzek.dive.util.ConfigurationManager;
import com.gmail.rohzek.dive.util.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SDiveGear extends ItemArmor
{
	float oldFlySpeed = -1f, newFlySpeed = 0.03f;
	
	public SDiveGear(String name, IArmorMaterial mat, EntityEquipmentSlot equipSlot) 
	{
		super(mat, equipSlot, new Item.Properties().group(Main.DIVE_GEAR_TAB).maxStackSize(1));
		setNames(name);
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
	public void onArmorTick(ItemStack stack, World world, EntityPlayer player) 
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
				LogHelper.debug("I'm in water!");
				addChanges(world, player, head, chest, legs, feet, above);
				
				// Just standing in water shouldn't use air, only being underwater
				if(above == Blocks.WATER || above == Blocks.SEAGRASS) // Check if we're in seagrass, too
				{
					LogHelper.debug("I'm underwater, damage the air tank!");
					damageTank(chest, player);
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
		   (chest.getDamage() < (chest.getMaxDamage() - 40))) 
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
		
		// If the boots and pants are on, grant easy movement through 'flying'
		if(legs != null && legs.getItem().equals(SArmor.DIVE_LEGS) && 
		   feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS))
		{
			if(oldFlySpeed == -1f)
			{
				oldFlySpeed = player.abilities.getFlySpeed();
			}
			
			if(world.isRemote) 
			{
				player.abilities.setFlySpeed(newFlySpeed);
			}
			
			player.abilities.isFlying = true;
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
				player.abilities.setFlySpeed(oldFlySpeed);
			}
			
			if(!player.isSpectator() && !player.isCreative()) 
			{
				player.abilities.isFlying = false;
			}
		}
	}
	
	private void repairTank(ItemStack chest, EntityPlayer player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{
			// By fdefault we refill with air twice as fast as it loses it 
			// (E.G. If you get 1 full minute of air, it takes 30 full seconds to refill)
			// But we allow up to 4 times faster
			if(chest.getDamage() < chest.getMaxDamage()) 
			{
				chest.damageItem(-(20 * ConfigurationManager.GENERAL.regainAirSpeed.get()), player);
			}
		}
	}
	
	private void damageTank(ItemStack chest, EntityPlayer player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.DIVE_CHEST)) 
		{	
			// We don't want to break the item, so only lower if we still have room to lower
			if(chest.getDamage() < (chest.getMaxDamage() - 21)) 
			{
				chest.damageItem(20, player);
				// If air tank is equipped underwater, after they've been breathing for awhile, refill the air instantly.
				player.setAir(player.getMaxAir());
			}
		}
	}
	
	// Was named onUpdate in previous versions, is now inventoryTick
	@SuppressWarnings("unused")
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) 
	{
		EntityPlayer player = (EntityPlayer) entity;
		Block above = world.getBlockState(new BlockPos(player.getPosition().getX(), player.getPosition().getY() + 1, player.getPosition().getZ())).getBlock();
		removeEnchantments(stack);
		
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
		if(!player.isCreative() && !player.isSpectator() && player.abilities.isFlying) 
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
					player.abilities.setFlySpeed(oldFlySpeed);
				}
				
				if(!player.isSpectator() && !player.isCreative()) 
				{
					player.abilities.isFlying = false;
				}
			}
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, EntityItem entity) 
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
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) 
	{
		// Have to return the exact path to the armor, just passing standard resource location won't work
		return Reference.RESOURCEID + "textures/models/armor/divegear" + (slot == EntityEquipmentSlot.LEGS ? "_layer_2" : "_layer_1") + ".png";
	}
	
	@Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        //super.addInformation(stack, worldIn, tooltip, flagIn);
        
        if(stack.getItem() == SArmor.DIVE_CHEST) 
        {
        	if(ConfigurationManager.GENERAL.consumeAir.get()) 
            {
            	long miliseconds = stack.getMaxDamage() - stack.getDamage();
            	long minutes = (miliseconds / 1000) / 60;
            	long seconds = (miliseconds / 1000) % 60;
                
            	if(minutes == 0 && seconds == 0 && stack.getDamage() == stack.getMaxDamage() - 20) 
            	{
            		tooltip.add(new TextComponentString("Air Tank Empty"));
            	}
            	else
            	{
            		tooltip.add(new TextComponentString("Air Left: " + minutes + ":" + (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : seconds)));
            	}
            }
        }
    }
}