package com.hytale.party.command.impl;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.messages.MessagesConfig;
import com.hytale.party.model.Party;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PartyDisbandSubCommand extends AbstractAsyncCommand {

    private final PartyCache partyCache;

    public PartyDisbandSubCommand(PartyCache partyCache) {
        super("disband", "Disband your current party.");
        this.partyCache = partyCache;
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(@NonNullDecl CommandContext commandContext) {
        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(MessagesConfig.ONLY_PLAYER);
            return CompletableFuture.completedFuture(null);
        }

        final Player player = (Player) commandContext.sender();
        final Ref<EntityStore> reference = player.getReference();
        if (reference == null) return CompletableFuture.completedFuture(null);

        final Store<EntityStore> store = reference.getStore();
        final World world = store.getExternalData().getWorld();
        world.execute(() -> {
            final PlayerRef playerRef = store.getComponent(reference, PlayerRef.getComponentType());
            if (playerRef == null) return;

            if (!partyCache.hasParty(playerRef.getUuid())) {
                player.sendMessage(MessagesConfig.NOT_IN_A_PARTY);
                return;
            }

            final Party party = partyCache.getByOwner(playerRef.getUuid());
            if(party == null) {
                player.sendMessage(MessagesConfig.ONLY_OWNER_CAN_DISBAND);
                return;
            }

            partyCache.remove(party);
            party.sendNotification(Message.raw("Party"), MessagesConfig.PARTY_DISBAND, NotificationStyle.Success);
        });

        return CompletableFuture.completedFuture(null);
    }
}
