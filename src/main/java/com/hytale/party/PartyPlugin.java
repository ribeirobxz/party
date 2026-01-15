package com.hytale.party;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.command.PartyCommand;
import com.hytale.party.system.DamageSystemListener;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class PartyPlugin extends JavaPlugin {
    public PartyPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        final PartyCache partyCache = new PartyCache();
        getCommandRegistry().registerCommand(new PartyCommand(partyCache));
        getEntityStoreRegistry().registerSystem(new DamageSystemListener(partyCache));

    }
}
