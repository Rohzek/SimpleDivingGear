package com.gmail.rohzek.divegear.lib;

import java.util.function.Supplier;

import com.gmail.rohzek.divegear.armor.SArmor;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DeferredRegistration 
{
	public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(Reference.MODID);
	//public static final DeferredRegister<MapCodec<? extends ConditionSource>> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.MATERIAL_CONDITION, Reference.MODID);
	private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Reference.MODID);

	public static void register(IEventBus bus)
	{
		//ARMOR_MATERIALS.register(bus);
		ITEMS.register(bus);
		TABS.register(bus);
	}

	public static final Supplier<CreativeModeTab> ITEM_GROUP = TABS.register(Reference.MODID, () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup." + Reference.MODID))
			.icon(() -> new ItemStack(SArmor.DIVE_CHEST.get()))
			.displayItems((params, output) -> {
				ITEMS.getEntries().forEach(entry -> {
					output.accept(entry.get());
				});
			})
			.build()
	);
}
