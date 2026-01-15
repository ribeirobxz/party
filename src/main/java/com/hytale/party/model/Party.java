package com.hytale.party.model;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class Party {

    private final UUID id = UUID.randomUUID();
    private final UUID ownerId;

    private final Set<UUID> members;
    private final Set<UUID> invites;

    private boolean publish = false;

    public boolean isLeader(@NonNull UUID playerID) {
        return ownerId.equals(playerID);
    }

    public boolean isMember(@NonNull UUID playerId) {
        return ownerId.equals(playerId) || members.contains(playerId);
    }

    public boolean isInvited(@NonNull UUID playerId) {
        return invites.contains(playerId);
    }

    public void addMember(@NonNull UUID playerId) {
        members.add(playerId);
    }

    public void addInvite(@NonNull UUID playerId) {
        invites.add(playerId);
    }

    public void removeMember(@NonNull UUID playerId) {
        members.remove(playerId);
    }

    public void removeInvite(@NonNull UUID playerId) {
        invites.remove(playerId);
    }

    public void sendMessage(Message message) {
        getPlayers().forEach(playerRef -> playerRef.sendMessage(message));
    }

    public void sendNotification(Message title, Message subTitle, NotificationStyle notificationStyle) {
        getPlayers().forEach(playerRef -> NotificationUtil.sendNotification(playerRef.getPacketHandler(), title, subTitle, "", new ItemStack("copper_shield").toPacket(), notificationStyle));
    }

    public void sendSound(int index, SoundCategory soundCategory) {
        getPlayers().forEach(playerRef -> SoundUtil.playSoundEvent2dToPlayer(playerRef, index, soundCategory, 1f, 1f));
    }

    public List<PlayerRef> getPlayers() {
        return members.stream().map(playerId -> Universe.get().getPlayer(playerId))
                .filter(playerRef -> playerRef != null && playerRef.isValid())
                .toList();
    }
}
