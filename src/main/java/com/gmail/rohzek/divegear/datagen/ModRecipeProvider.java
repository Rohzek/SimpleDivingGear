package com.gmail.rohzek.divegear.datagen;

import java.util.concurrent.CompletableFuture;

import com.gmail.rohzek.divegear.armor.SArmor;
import com.gmail.rohzek.divegear.items.DiveGearItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder 
{
	public ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) 
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) 
	{
		bootsUpgrades(recipeOutput);
		chestplateUpgrades(recipeOutput);
		helmetUpgrades(recipeOutput);
		leggingsUpgrades(recipeOutput);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DiveGearItems.DIVE_HELMET_CORE)
				.define('P', Tags.Items.GLASS_PANES)
				.pattern("PPP")
				.pattern("P P")
				.pattern("PPP")
				.unlockedBy("has_redstone", has(Items.REDSTONE))
				.save(recipeOutput);
	}
	
	private static void bootsUpgrades(RecipeOutput recipeOutput) 
	{
		boots(recipeOutput, Tags.Items.LEATHERS, Tags.Items.OBSIDIANS, Tags.Items.DYES_ORANGE, SArmor.DIVE_BOOTS.get());
		bootsn(recipeOutput, Tags.Items.LEATHERS,  Items.NETHERITE_SCRAP, Tags.Items.DYES_RED, SArmor.NETHER_DIVE_BOOTS.get());
	}

	private static void chestplateUpgrades(RecipeOutput recipeOutput) 
	{
		chestplate(recipeOutput, Tags.Items.INGOTS_IRON, Tags.Items.OBSIDIANS, SArmor.DIVE_CHEST.get());
		chestplaten(recipeOutput, Tags.Items.INGOTS_IRON, Items.NETHERITE_SCRAP, SArmor.NETHER_DIVE_CHEST.get());
	}

	private static void helmetUpgrades(RecipeOutput recipeOutput) 
	{
		helmet(recipeOutput, Tags.Items.GLASS_BLOCKS_TINTED, DiveGearItems.DIVE_HELMET_CORE, SArmor.DIVE_HELMET.get());
		helmetn(recipeOutput, Items.NETHERITE_SCRAP, DiveGearItems.DIVE_HELMET_CORE, SArmor.NETHER_DIVE_HELMET.get());
		
		helmet_lighted(recipeOutput, Tags.Items.DUSTS_GLOWSTONE, SArmor.DIVE_HELMET.get(), Tags.Items.DUSTS_REDSTONE, SArmor.DIVE_HELMET_LIGHTS.get());
		helmet_lighted(recipeOutput, Tags.Items.DUSTS_GLOWSTONE, SArmor.NETHER_DIVE_HELMET.get(), Tags.Items.DUSTS_REDSTONE, SArmor.NETHER_DIVE_HELMET_LIGHTS.get());
	}

	private static void leggingsUpgrades(RecipeOutput recipeOutput) 
	{
		leggings(recipeOutput, Tags.Items.LEATHERS, Tags.Items.OBSIDIANS, Tags.Items.DYES_ORANGE, SArmor.DIVE_LEGS.get());
		leggings(recipeOutput, Tags.Items.LEATHERS, Tags.Items.OBSIDIANS, Tags.Items.DYES_RED, SArmor.NETHER_DIVE_LEGS.get());
	}

	protected static void boots(RecipeOutput recipeOutput, TagKey<Item> material_base, TagKey<Item> material_block, TagKey<Item> material_color, ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_base)
				.define('B', material_block)
				.define('D', material_color)
				.pattern("XBX")
				.pattern("XDX")
				.unlockedBy("has_material", has(material_base))
				.save(recipeOutput);
	}
	
	protected static void bootsn(RecipeOutput recipeOutput, TagKey<Item> material_base, ItemLike material_block, TagKey<Item> material_color, ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_base)
				.define('B', material_block)
				.define('D', material_color)
				.pattern("XBX")
				.pattern("XDX")
				.unlockedBy("has_material", has(material_base))
				.save(recipeOutput);
	}

	protected static void chestplate(RecipeOutput recipeOutput, TagKey<Item> material_base, TagKey<Item> material_block,  ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_base)
				.define('B', material_block)
				.pattern("XBX")
				.pattern("B B")
				.pattern("XBX")
				.unlockedBy("has_material", has(material_base))
				.save(recipeOutput);
	}
	
	protected static void chestplaten(RecipeOutput recipeOutput, TagKey<Item> material_base, ItemLike material_block,  ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_base)
				.define('B', material_block)
				.pattern("XBX")
				.pattern("B B")
				.pattern("XBX")
				.unlockedBy("has_material", has(material_base))
				.save(recipeOutput);
	}

	protected static void helmet(RecipeOutput recipeOutput,  TagKey<Item> material_base, ItemLike material_block, ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_base)
				.define('C', material_block)
				.pattern("XXX")
				.pattern("XCX")
				.unlockedBy("has_material", has(material_base))
				.save(recipeOutput);
	}
	
	protected static void helmetn(RecipeOutput recipeOutput,  ItemLike material_base, ItemLike material_block, ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_base)
				.define('C', material_block)
				.pattern("XXX")
				.pattern("XCX")
				.unlockedBy("has_material", has(material_base))
				.save(recipeOutput);
	}
	
	protected static void helmet_lighted(RecipeOutput recipeOutput, TagKey<Item> material_light, ItemLike material_helmet, TagKey<Item> material_power, ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_helmet)
				.define('L', material_light)
				.define('P', material_power)
				.pattern("L")
				.pattern("X")
				.pattern("P")
				.unlockedBy("has_material", has(material_helmet))
				.save(recipeOutput);
	}

	protected static void leggings(RecipeOutput recipeOutput, TagKey<Item> material_base, TagKey<Item> material_block, TagKey<Item> material_color, ItemLike result) 
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
				.define('X', material_base)
				.define('B', material_block)
				.define('D', material_color)
				.pattern("XXX")
				.pattern("XBX")
				.pattern("XDX")
				.unlockedBy("has_material", has(material_base))
				.save(recipeOutput);
	}

	/*
	protected static void materialUpgrade(RecipeOutput recipeOutput, Item baseItem, Ingredient upgradeItem, RecipeCategory category, Item resultItem, Boolean isHard) {
		materialUpgrade(recipeOutput, baseItem, upgradeItem, category, resultItem, isHard, null);
	}

	protected static void materialUpgrade(RecipeOutput recipeOutput, Item baseItem, Ingredient upgradeItem, RecipeCategory category, Item resultItem, Boolean isHard, String suffix) {
		AUSUpgradeRecipeBuilder builder = AUSUpgradeRecipeBuilder.smithing(
						Ingredient.of(AUSItems.SMITHING_TEMPLATE), Ingredient.of(baseItem), upgradeItem, category, resultItem
				)
				.unlocks("has_upgrade_item", has(upgradeItem.getItems()[0].getItem()));
				if (isHard != null) builder.addCondition(isHard ? new ConfigCondition() : new NotCondition(new ConfigCondition()));
				builder.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "upgrade/" + (isHard == null ? "" : (isHard ? "hard/" : "easy/")) + getItemName(resultItem) + (suffix != null ? suffix : "")));
	}
	
	protected static void materialUpgradeVanillaStyle(RecipeOutput recipeOutput, Item baseItem, Ingredient upgradeItem, RecipeCategory category, Item resultItem, Boolean isHard) {
		materialUpgradeVanillaStyle(recipeOutput, baseItem, upgradeItem, category, resultItem, isHard, null);
	}
	
	protected static void materialUpgradeVanillaStyle(RecipeOutput recipeOutput, Item baseItem, Ingredient upgradeItem, RecipeCategory category, Item resultItem, Boolean isHard, String suffix) {
		AUSUpgradeRecipeBuilder builder = AUSUpgradeRecipeBuilder.smithing(
						Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(baseItem), upgradeItem, category, resultItem
				)
				.unlocks("has_upgrade_item", has(upgradeItem.getItems()[0].getItem()));
				if (isHard != null) builder.addCondition(isHard ? new ConfigCondition() : new NotCondition(new ConfigCondition()));
				builder.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "upgrade/" + (isHard == null ? "" : (isHard ? "hard/" : "easy/")) + getItemName(resultItem) + (suffix != null ? suffix : "")));
	}
	*/
}
