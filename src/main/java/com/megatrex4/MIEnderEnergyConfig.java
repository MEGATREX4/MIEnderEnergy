package com.megatrex4;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.util.Identifier;

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
    }
}
