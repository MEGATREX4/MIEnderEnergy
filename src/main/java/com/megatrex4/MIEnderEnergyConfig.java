package com.megatrex4;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class MIEnderEnergyConfig {

    public static Server SERVER = ConfigApiJava.registerAndLoadConfig(Server::new, RegisterType.BOTH);

    @Version(version = 1)
    public static class Server extends Config {

        public Server() {
            super(new Identifier(MIEnderEnergy.MOD_ID, "server-config"));
        }

        @Comment("The maximum amount of energy that can be extracted per tick")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long MAX_EXTRACT = 1_000_000_000L;

        @Comment("The maximum amount of energy that can be inserted per tick")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long MAX_INSERT = 1_000_000_000L;

        @Comment("The total amount of energy the network can store")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long MAX_NETWORK_ENERGY = 1_000_000_000_000_000_000L;



        @Comment("Capacity of LV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long LV_CAPACITY = 19_200L;

        @Comment("Extraction rate of LV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long LV_EXTRACTION_RATE = ((LV_CAPACITY / 8));

        @Comment("Capacity of MV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long MV_CAPACITY = 76_800L;

        @Comment("Extraction rate of MV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long MV_EXTRACTION_RATE = ((MV_CAPACITY / 8));

        @Comment("Capacity of HV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long HV_CAPACITY = 614_400L;

        @Comment("Extraction rate of HV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long HV_EXTRACTION_RATE = ((HV_CAPACITY / 8));

        @Comment("Capacity of EV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long EV_CAPACITY = 4_915_200L;

        @Comment("Extraction rate of EV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long EV_EXTRACTION_RATE = ((EV_CAPACITY / 8));

        @Comment("Capacity of IV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long IV_CAPACITY = 76_800_000_000L;

        @Comment("Extraction rate of IV solar panels")
        @ValidatedFloat.Restrict(min = 1L, max = Long.MAX_VALUE)
        public long IV_EXTRACTION_RATE = ((IV_CAPACITY / 8));

        @Comment("World-specific energy multipliers")
        public Map<String, Float> WORLD_MULTIPLIERS = new HashMap<String, Float>() {{
            put("minecraft:nether", 0f);
            put("ad_astra:moon", 2.0f);
            put("ad_astra:mercury", 3.0f);
        }};

    }
}
