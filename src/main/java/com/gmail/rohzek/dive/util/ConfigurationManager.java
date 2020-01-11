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
        public final ForgeConfigSpec.ConfigValue<Boolean> displayAirRemaining;
        public final ForgeConfigSpec.ConfigValue<Float> airDisplayVerticalAlignment;
        public final ForgeConfigSpec.ConfigValue<Boolean> airRemainingCustomLocation;
        public final ForgeConfigSpec.ConfigValue<Integer> airDisplayCustomX;
        public final ForgeConfigSpec.ConfigValue<Integer> airDisplayCustomY;
        public final ForgeConfigSpec.ConfigValue<Integer> minutesOfAir;
        public final ForgeConfigSpec.ConfigValue<Integer> regainAirSpeed;
        public final ForgeConfigSpec.ConfigValue<Boolean> invincibleArmor;

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
            
            displayAirRemaining = builder
                    .comment("Enables/Disables the rendering of air time left in tank [false/true|default:true]")
                    .translation("displayairremaining.simpledivegear.config")
                    .define("displayAirRemaining", true);
            
            airDisplayVerticalAlignment = builder
                    .comment("Vertical alignment of the air display text. Is a float, so a number + f is required. Ignored when custom location is enabled [default:69f]")
                    .translation("displaycustomx.simpledivegear.config")
                    .define("airDisplayVerticalAlignment", 69f);
            
            airRemainingCustomLocation = builder
                    .comment("Enables/Disables a custom location for the air display [false/true|default:false]")
                    .translation("airremainingcustomlocation.simpledivegear.config")
                    .define("airRemainingCustomLocation", false);
            
            airDisplayCustomX = builder
                    .comment("Allows for a completely custom X value for the location of the air counter. Is resolution dependant! [default:0]")
                    .translation("displaycustomx.simpledivegear.config")
                    .define("airDisplayCustomX", 0);
            
            airDisplayCustomY = builder
                    .comment("Allows for a completely custom Y value for the location of the air counter. Is resolution dependant! [default:0]")
                    .translation("displaycustomy.simpledivegear.config")
                    .define("airDisplayCustomY", 0);
            
            invincibleArmor = builder
                    .comment("If the head, legs, and feet should repair themselves if damage is recieved [false/true|default:true]")
                    .translation("invincibleArmor.simpledivegear.config")
                    .define("invincibleArmor", true);
            
            builder.pop();
        }
    }
}