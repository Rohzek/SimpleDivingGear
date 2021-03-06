package com.gmail.rohzek.dive.armor;

import java.util.List;
import java.util.Map;

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

public class SNetherDiveGear extends ArmorItem
{
float oldFlySpeed = -1f, newFlySpeed = 0.03f;
	
	public SNetherDiveGear(String name, IArmorMaterial mat, EquipmentSlotType equipSlot) 
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
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) 
	{	
		repairArmor(player.inventory.armorInventory);
		
		if(!player.isCreative() && !player.isSpectator()) 
		{
			Block above = world.getBlockState(new BlockPos(player.getPosX(), player.getPosY() + 1, player.getPosZ())).getBlock();
			
			NonNullList<ItemStack> armorSlots = player.inventory.armorInventory;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			addChanges(world, player, head, chest, legs, feet, above);
			player.extinguish();
			
			if(player.isInLava())
			{
				LogHelper.debug("I'm in lava!");
				
				// Only damage the tank if we're consuming air, which we can only do with a helmet and the chest piece
				if(above == Blocks.LAVA) 
				{
					if((head.getItem() == SArmor.NETHER_DIVE_HELMET || head.getItem() == SArmor.NETHER_DIVE_HELMET_LIGHTS) && chest.getItem() == SArmor.NETHER_DIVE_CHEST) 
					{
						LogHelper.debug("I'm under lava, damage the air tank!");
						damageTank(chest, player);
					}
				}
				
				else // If your head is above lava, then you should still get air back
				{
					LogHelper.debug("I'm above lava, repair the air tank!");
					repairTank(chest, player);
				}
			}
			
			else 
			{
				LogHelper.debug("I'm not in lava! Repair the tank and remove buffs");
				repairTank(chest, player);
				removeChanges(world, player, head, chest, legs, feet);
			}
		}
		
		else if(player.isCreative() || player.isSpectator() && player.isInLava())
		{
			// Even in Creative mode
			Block above = world.getBlockState(new BlockPos(player.getPosX(), player.getPosY() + 1, player.getPosZ())).getBlock();			
			
			NonNullList<ItemStack> armorSlots = player.inventory.armorInventory;
						
			ItemStack head = armorSlots.get(3);
			
			if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS) && above == Blocks.LAVA) 
			{
				player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 2, 0, false, false));
			}
		}
	}
	
	private void addChanges(World world, PlayerEntity player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet, Block above) 
	{
		if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS) && above == Blocks.LAVA) 
		{
			player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 2, 0, false, false));
		}
		
		// If boots are on, grant depth strider
		if(feet != null && feet.getItem().equals(SArmor.NETHER_DIVE_BOOTS)) 
		{
			if(EnchantmentHelper.getEnchantments(feet).get(Enchantments.DEPTH_STRIDER) == null)
			{
				feet.addEnchantment(Enchantments.DEPTH_STRIDER, 1);
			}
		}
		
		// If the boots and pants are on, grant easy movement through 'flying'
		if(legs != null && legs.getItem().equals(SArmor.NETHER_DIVE_LEGS) && 
		   feet != null && feet.getItem().equals(SArmor.NETHER_DIVE_BOOTS))
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
		
		// Requires all components to receive the fire resistance
		if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET) || head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS) &&
		   chest != null && chest.getItem().equals(SArmor.NETHER_DIVE_CHEST) && legs != null && legs.getItem().equals(SArmor.NETHER_DIVE_LEGS) &&
		   feet != null && feet.getItem().equals(SArmor.NETHER_DIVE_BOOTS)) 
		{	
			player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2, 0, false, false));
		}
	}
	
	private void removeChanges(World world, PlayerEntity player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) 
	{
		if(feet != null && feet.getItem().equals(SArmor.DIVE_BOOTS)) 
		{
			removeEnchantments(feet);
		}
		
		if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET) ||
		   head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS) ||
		   chest != null && chest.getItem().equals(SArmor.NETHER_DIVE_CHEST) ||
		   legs != null && legs.getItem().equals(SArmor.NETHER_DIVE_LEGS) || 
		   feet != null && feet.getItem().equals(SArmor.NETHER_DIVE_BOOTS))
		{
			player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 0, false, false));
			player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 2, 0, false, false));
			
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
	
	private void repairTank(ItemStack chest, PlayerEntity player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.NETHER_DIVE_CHEST)) 
		{
			// By fdefault we refill with air twice as fast as it loses it 
			// (E.G. If you get 1 full minute of air, it takes 30 full seconds to refill)
			// But we allow up to 4 times faster
			if(chest.getDamage() < chest.getMaxDamage()) 
			{
				chest.damageItem(-(20 * ConfigurationManager.GENERAL.regainAirSpeed.get()), player, null);
			}
		}
	}
	
	private void damageTank(ItemStack chest, PlayerEntity player) 
	{
		if(ConfigurationManager.GENERAL.consumeAir.get() && chest.getItem().equals(SArmor.NETHER_DIVE_CHEST)) 
		{	
			// We don't want to break the item, so only lower if we still have room to lower
			if(chest.getDamage() < (chest.getMaxDamage() - 21)) 
			{
				chest.damageItem(20, player, null);
			}
		}
	}
	
	private void repairArmor(NonNullList<ItemStack> armorSlots) 
	{
		if(ConfigurationManager.GENERAL.invincibleArmor.get()) 
		{
			ItemStack head = armorSlots.get(3),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET) || 
			   head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS)) 
			{
				if(head.isDamaged()) 
				{
					head.setDamage(0);
				}
			}
			
			if(legs != null && legs.getItem().equals(SArmor.NETHER_DIVE_LEGS)) 
			{
				if(legs.isDamaged()) 
				{
					legs.setDamage(0);
				}
			}
			
			if(feet != null && feet.getItem().equals(SArmor.NETHER_DIVE_BOOTS)) 
			{
				if(feet.isDamaged()) 
				{
					feet.setDamage(0);
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
		Block above = world.getBlockState(new BlockPos(player.getPosX(), player.getPosY() + 1, player.getPosZ())).getBlock();
		removeEnchantments(stack);
		
		repairArmor(player.inventory.armorInventory);
		
		// If you're not in water, then get air back
		if(!player.isInLava()) 
		{
			repairTank(stack, player);
		}
		// If you're in water but not not underwater, get air back
		else if(player.isInLava() && above != Blocks.LAVA)
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
			
			if(legs != null && !legs.getItem().equals(SArmor.NETHER_DIVE_LEGS) ||
			   feet != null && !feet.getItem().equals(SArmor.NETHER_DIVE_BOOTS)) 
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
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) 
	{
		// Have to return the exact path to the armor, just passing standard resource location won't work
		return Reference.RESOURCEID + "textures/models/armor/netherdivegear" + (slot == EquipmentSlotType.LEGS ? "_layer_2" : "_layer_1") + ".png";
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) 
	{
		if(stack.getItem() == SArmor.NETHER_DIVE_CHEST) 
        {
        	long miliseconds = stack.getMaxDamage() - stack.getDamage();
        	long minutes = (miliseconds / 1000) / 60;
        	long seconds = (miliseconds / 1000) % 60;
            
        	if(minutes == 0 && seconds == 0 && stack.getDamage() == stack.getMaxDamage() - 20) 
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
