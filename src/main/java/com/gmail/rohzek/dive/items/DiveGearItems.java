package com.gmail.rohzek.dive.items;

import com.gmail.rohzek.dive.lib.DeferredRegistration;

import net.minecraftforge.registries.RegistryObject;

public class DiveGearItems 
{
    public static final RegistryObject<DiveItem> DIVE_HELMET_CORE = DeferredRegistration.ITEMS.register("divehelmetcore", () -> new DiveItem());
    
    public static void register() {}
}