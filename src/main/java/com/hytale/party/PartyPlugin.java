package com.hytale.party;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.command.PartyCommand;
import com.hytale.party.compass.PartyMemberMarkerProvider;
import com.hytale.party.system.DamageSystemListener;
import lombok.Getter;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class PartyPlugin extends JavaPlugin {

    private static PartyPlugin INSTANCE;

    @Getter
    private PartyCache partyCache;

    public PartyPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    public static PartyPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    protected void setup() {
        super.setup();

        INSTANCE = this;
        this.partyCache = new PartyCache();

        getCommandRegistry().registerCommand(new PartyCommand(partyCache));
        getEntityStoreRegistry().registerSystem(new DamageSystemListener(partyCache));

        getEventRegistry().registerGlobal(AddWorldEvent.class, event -> event.getWorld()
                .getWorldMapManager()
                .addMarkerProvider("partyMembers", PartyMemberMarkerProvider.INSTANCE));
    }
}
