package com.hytale.party.command.impl;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.messages.MessagesConfig;
import com.hytale.party.model.Party;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.concurrent.CompletableFuture;

public class PartyJoinSubCommand extends AbstractAsyncCommand {

    private final PartyCache partyCache;
    private RequiredArg<PlayerRef> targetRefArg;

    public PartyJoinSubCommand(PartyCache partyCache) {
        super("join", "Join in a party");
        this.partyCache = partyCache;

        this.targetRefArg = this.withRequiredArg("player", "target player", ArgTypes.PLAYER_REF);
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

            if (partyCache.hasParty(playerRef.getUuid())) {
                playerRef.sendMessage(MessagesConfig.ALREADY_IN_A_PARTY);
                return;
            }

            final PlayerRef targetRef = commandContext.get(targetRefArg);
            if (targetRef == null) {
                playerRef.sendMessage(MessagesConfig.PLAYER_NOT_FOUND);
                return;
            }

            final Party party = partyCache.getByOwner(targetRef.getUuid());
            if (party == null) {
                playerRef.sendMessage(MessagesConfig.PLAYER_DONT_HAVE_PARTY);
                return;
            }

            if (!party.isPublish()) {
                if (!party.isInvited(playerRef.getUuid())) {
                    playerRef.sendMessage(MessagesConfig.PLAYER_DONT_HAVE_INVITE);
                    return;
                }

                party.removeInvite(playerRef.getUuid());
            }

            party.addMember(playerRef.getUuid());

            party.sendSound(SoundEvent.getAssetMap().getIndex("SFX_Cactus_Large_Hit"), SoundCategory.UI);
            party.sendNotification(Message.raw("Party"), MessagesConfig.PLAYER_JOIN_PARTY.param("username", playerRef.getUsername()), NotificationStyle.Success);

            EventTitleUtil.showEventTitleToPlayer(playerRef, Message.raw("PARTY"), MessagesConfig.PLAYER_JOIN_PARTY_TITLE.param("party-owner", targetRef.getUsername()), true);
        });

        return CompletableFuture.completedFuture(null);
    }
}
