package com.hytale.party.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Party {

    private final UUID id = UUID.randomUUID();
    private final UUID ownerId;
    private final Set<UUID> members;

    public boolean isLeader(@NonNull UUID playerID) {
        return ownerId.equals(playerID);
    }

    public boolean isMember(@NonNull UUID playerId) {
        return ownerId.equals(playerId) || members.contains(playerId);
    }

    public void addMember(@NonNull UUID playerId) {
        members.add(playerId);
    }
}
