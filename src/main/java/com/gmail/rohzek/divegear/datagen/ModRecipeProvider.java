package com.gmail.rohzek.divegear.datagen;

import java.util.concurrent.CompletableFuture;

import com.gmail.rohzek.divegear.armor.SArmor;
import com.gmail.rohzek.divegear.items.DiveGearItems;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder 
{
	RecipeOutput recipeOutput;
	
	protected ModRecipeProvider(Provider registries, RecipeOutput recipeOutput) 
	{
		super(registries, recipeOutput);
		
		this.recipeOutput = recipeOutput;
	}
	
	public static class Runner extends RecipeProvider.Runner
	{

		public Runner(PackOutput packOutput, CompletableFuture<Provider> provider) 
		{
			super(packOutput, provider);
		}

		@Override
		public String getName() 
		{
			return "DiveGearRecipes";
		}

		@Override
		protected RecipeProvider createRecipeProvider(Provider registries, RecipeOutput recipeOutput) 
		{
			return new ModRecipeProvider(registries, recipeOutput);
		}
		
	}

	@Override
	protected void buildRecipes() 
	{
		// Core Item
		shaped(RecipeCategory.MISC, DiveGearItems.DIVE_HELMET_CORE)
			.define('P', Tags.Items.GLASS_PANES)
			.pattern("PPP")
			.pattern("P P")
			.pattern("PPP")
			.unlockedBy("has_redstone", has(Items.REDSTONE))
			.save(recipeOutput);
		// Helmets
		shaped(RecipeCategory.COMBAT, SArmor.DIVE_HELMET.get())
			.define('X', Tags.Items.GLASS_BLOCKS_TINTED)
			.define('C',  DiveGearItems.DIVE_HELMET_CORE)
			.pattern("XXX")
			.pattern("XCX")
			.unlockedBy("has_material", has( DiveGearItems.DIVE_HELMET_CORE))
			.save(recipeOutput);
		shaped(RecipeCategory.COMBAT, SArmor.NETHER_DIVE_HELMET.get())
			.define('X', Items.NETHERITE_SCRAP)
			.define('C',  DiveGearItems.DIVE_HELMET_CORE)
			.pattern("XXX")
			.pattern("XCX")
			.unlockedBy("has_material", has( DiveGearItems.DIVE_HELMET_CORE))
			.save(recipeOutput);
		// Lighted Helmets
		shapeless(RecipeCategory.COMBAT, SArmor.DIVE_HELMET_LIGHTS.get())
			.requires(Tags.Items.DUSTS_GLOWSTONE)
			.requires(SArmor.DIVE_HELMET.get())
			.requires(Tags.Items.DUSTS_REDSTONE)
			.unlockedBy("has_material", has(SArmor.DIVE_HELMET.get()))
			.save(recipeOutput);
		shapeless(RecipeCategory.COMBAT, SArmor.NETHER_DIVE_HELMET_LIGHTS.get())
			.requires(Tags.Items.DUSTS_GLOWSTONE)
			.requires(SArmor.NETHER_DIVE_HELMET.get())
			.requires(Tags.Items.DUSTS_REDSTONE)
			.unlockedBy("has_material", has(SArmor.NETHER_DIVE_HELMET.get()))
			.save(recipeOutput);
		// Chestplates
		shaped(RecipeCategory.COMBAT, SArmor.DIVE_CHEST.get())
			.define('X', Tags.Items.INGOTS_IRON)
			.define('B', Tags.Items.OBSIDIANS)
			.pattern("XBX")
			.pattern("B B")
			.pattern("XBX")
			.unlockedBy("has_material", has(Tags.Items.INGOTS_IRON))
			.save(recipeOutput);
		shaped(RecipeCategory.COMBAT, SArmor.NETHER_DIVE_CHEST.get())
			.define('X', Tags.Items.INGOTS_IRON)
			.define('B', Items.NETHERITE_SCRAP)
			.pattern("XBX")
			.pattern("B B")
			.pattern("XBX")
			.unlockedBy("has_material", has(Tags.Items.INGOTS_IRON))
			.save(recipeOutput);
		// Leggings
		shaped(RecipeCategory.COMBAT, SArmor.DIVE_LEGS.get())
			.define('X', Tags.Items.LEATHERS)
			.define('B', Tags.Items.OBSIDIANS)
			.define('D', Tags.Items.DYES_ORANGE)
			.pattern("XXX")
			.pattern("XBX")
			.pattern("XDX")
			.unlockedBy("has_material", has(Tags.Items.LEATHERS))
			.save(recipeOutput);
		shaped(RecipeCategory.COMBAT, SArmor.NETHER_DIVE_LEGS.get())
			.define('X', Tags.Items.LEATHERS)
			.define('B', Tags.Items.OBSIDIANS)
			.define('D', Tags.Items.DYES_RED)
			.pattern("XXX")
			.pattern("XBX")
			.pattern("XDX")
			.unlockedBy("has_material", has(Tags.Items.LEATHERS))
			.save(recipeOutput);
		// Boots
		shaped(RecipeCategory.COMBAT, SArmor.DIVE_BOOTS.get())
			.define('X', Tags.Items.LEATHERS)
			.define('B', Tags.Items.OBSIDIANS)
			.define('D', Tags.Items.DYES_ORANGE)
			.pattern("XBX")
			.pattern("XDX")
			.unlockedBy("has_material", has(Tags.Items.LEATHERS))
			.save(recipeOutput);
		
		shaped(RecipeCategory.COMBAT, SArmor.NETHER_DIVE_BOOTS.get())
			.define('X', Tags.Items.LEATHERS)
			.define('B', Items.NETHERITE_SCRAP)
			.define('D', Tags.Items.DYES_RED)
			.pattern("XBX")
			.pattern("XDX")
			.unlockedBy("has_material", has(Tags.Items.LEATHERS))
			.save(recipeOutput);
	}

	/*
	protected static void materialUpgrade(RecipeOutput recipeOutput, Item baseItem, Ingredient upgradeItem, RecipeCategory category, Item resultItem, Boolean isHard) {
		materialUpgrade(recipeOutput, basadeRecipeBuilder.smithing(
						Ingredient.of(AUSItems.SMITHING_TEMPLeItem, upgradeItem, category, resultItem, isHard, null);
	}

	protected static void materialUpgrade(RecipeOutput recipeOutput, Item baseItem, Ingredient upgradeItem, RecipeCategory category, Item resultItem, Boolean isHard, String suffix) {
		AUSUpgradeRecipeBuilder builder = AUSUpgrATE), Ingredient.of(baseItem), upgradeItem, category, resultItem
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
