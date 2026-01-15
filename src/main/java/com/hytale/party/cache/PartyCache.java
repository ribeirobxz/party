package com.hytale.party.cache;

import com.hytale.party.model.Party;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyCache {

    private final Map<UUID, Party> cache = new HashMap<>();

    public void add(Party party) {
        cache.put(party.getId(), party);
    }

    public void remove(Party party) {
        cache.remove(party.getId());
    }

    public Party getParty(@NonNull UUID playerId) {
        return cache.values().stream().filter(party -> party.isMember(playerId)).findFirst().orElse(null);
    }

    public Party getByOwner(@NonNull UUID ownerId) {
        return cache.values().stream().filter(party -> party.isLeader(ownerId)).findFirst().orElse(null);
    }

    public boolean hasParty(@NonNull UUID playerId) {
        return cache.values().stream().anyMatch(party -> party.isMember(playerId));
    }

    public Collection<Party> getParties() {
        return cache.values();
    }

}
