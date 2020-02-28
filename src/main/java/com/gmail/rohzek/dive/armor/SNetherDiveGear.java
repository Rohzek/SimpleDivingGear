package com.gmail.rohzek.dive.armor;

import java.util.List;

import com.gmail.rohzek.dive.lib.Reference;
import com.gmail.rohzek.dive.main.Main;
import com.gmail.rohzek.dive.util.ConfigurationManager;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.text.TextFormatting;
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
		if(!player.isCreative() && !player.isSpectator()) 
		{
			Block above = world.getBlockState(new BlockPos(player.getPosition().getX(), player.getPosition().getY() + 1, player.getPosition().getZ())).getBlock();
			
			NonNullList<ItemStack> armorSlots = player.inventory.armorInventory;
			
			ItemStack head = armorSlots.get(3),
					  chest = armorSlots.get(2),
					  legs = armorSlots.get(1),
					  feet = armorSlots.get(0);
			
			addChanges(world, player, head, chest, legs, feet, above);
			
			/*
			if(player.isInLava())
			{
				LogHelper.debug("I'm in lava!");
				addChanges(world, player, head, chest, legs, feet, above);
			}
			
			else 
			{
				LogHelper.debug("I'm not in lava!");
				removeChanges(world, player, head, chest, legs, feet);
			}
			*/
		}
	}
	
	private void addChanges(World world, PlayerEntity player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet, Block above) 
	{
		// Testing to make us live in lava
		if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET) || head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS)) 
		{	
			player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2, 0, false, false));
		}
		
		if(head != null && head.getItem().equals(SArmor.NETHER_DIVE_HELMET_LIGHTS)) 
		{
			player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 2, 0, false, false));
		}
	}
	
	//private void removeChanges(World world, PlayerEntity player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {}
	
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
		return Reference.RESOURCEID + "textures/models/armor/divegear" + (slot == EquipmentSlotType.LEGS ? "_layer_2" : "_layer_1") + ".png";
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) 
	{
			tooltip.add(new StringTextComponent(TextFormatting.RED + "IN TESTING, PLEASE IGNORE"));
		}
}
