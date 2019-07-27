package com.gmail.rohzek.dive.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigurationManager
{
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    public static class General 
    {
    	public final ForgeConfigSpec.ConfigValue<Boolean> isDebug;
        public final ForgeConfigSpec.ConfigValue<Boolean> consumeAir;
        public final ForgeConfigSpec.ConfigValue<Integer> minutesOfAir;
        public final ForgeConfigSpec.ConfigValue<Integer> regainAirSpeed;

        public General(ForgeConfigSpec.Builder builder) 
        {
            builder.push("General");
            
            isDebug = builder
                    .comment("Enables/Disables debug mode (SPAMS LOGS! Is for detailed bug reports; You probably don't want this for normal play) [false/true|default:false]")
                    .translation("debugmode.simpledivegear.config")
                    .define("isDebug", false);
            
            consumeAir = builder
                    .comment("Enables/Disables the limited air system [false/true|default:true]")
                    .translation("shouldconsumeair.simpledivegear.config")
                    .define("consumeAir", true);
            
            minutesOfAir = builder
                    .comment("How many minutes of air do you have on one tank [1..10|default:2]")
                    .translation("minutespertank.simpledivegear.config")
                    .defineInRange("minutesOfAir", 2, 1, 10);
            
            regainAirSpeed = builder
                    .comment("How quickly should the air return 1x the speed it takes to lose it, 2x the speed, etc. [1..4|default:2]")
                    .translation("minutespertank.simpledivegear.config")
                    .defineInRange("regainAirSpeed", 2, 1, 4);
            
            builder.pop();
        }
    }
}