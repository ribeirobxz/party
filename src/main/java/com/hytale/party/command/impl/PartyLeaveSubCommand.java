package com.hytale.party.command.impl;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.messages.MessagesConfig;
import com.hytale.party.model.Party;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.concurrent.CompletableFuture;

public class PartyLeaveSubCommand extends AbstractAsyncCommand {

    private final PartyCache partyCache;

    public PartyLeaveSubCommand(PartyCache partyCache) {
        super("leave", "Leave your current party.");
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

            final Party party = partyCache.getParty(playerRef.getUuid());
            if(party == null) {
                player.sendMessage(MessagesConfig.NOT_IN_A_PARTY);
                return;
            }

            if(party.isLeader(playerRef.getUuid())) {
                player.sendMessage(MessagesConfig.OWNER_CANT_LEAVE_PARTY);
                return;
            }

            party.removeMember(playerRef.getUuid());

            party.sendSound(SoundEvent.getAssetMap().getIndex("SFX_Cactus_Large_Hit"), SoundCategory.UI);
            party.sendNotification(Message.raw("Party"), MessagesConfig.PLAYER_LEAVE_PARTY.param("username", playerRef.getUsername()), NotificationStyle.Success);

            EventTitleUtil.showEventTitleToPlayer(playerRef, Message.raw("PARTY"), MessagesConfig.PLAYER_LEAVE_PARTY_TITLE, true);
        });

        return CompletableFuture.completedFuture(null);
    }
}
