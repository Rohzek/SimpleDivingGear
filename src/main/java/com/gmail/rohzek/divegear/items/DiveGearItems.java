package com.gmail.rohzek.divegear.items;

import com.gmail.rohzek.divegear.lib.DeferredRegistration;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class DiveGearItems 
{
    public static final DeferredItem<Item> DIVE_HELMET_CORE = DeferredRegistration.ITEMS.register("divehelmetcore", () -> new DiveItem());
    
    public static void register() {}
}
