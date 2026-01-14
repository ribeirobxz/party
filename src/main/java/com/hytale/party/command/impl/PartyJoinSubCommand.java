package com.hytale.party.command.impl;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.model.Party;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PartyJoinSubCommand extends AbstractAsyncCommand {

    private final PartyCache partyCache;
    private RequiredArg<PlayerRef> targetRefArg;

    public PartyJoinSubCommand(PartyCache partyCache) {
        super("entrar", "Entre em uma party de algum amigo.");
        this.partyCache = partyCache;

        this.targetRefArg = this.withRequiredArg("player", "Jogador alvo", ArgTypes.PLAYER_REF);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(@NonNullDecl CommandContext commandContext) {
        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Esse comando é somente para jogadores.").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        final Player player = (Player) commandContext.sender();
        final Ref<EntityStore> reference = player.getReference();
        if (reference == null)   return CompletableFuture.completedFuture(null);

        final Store<EntityStore> store = reference.getStore();
        final World world = store.getExternalData().getWorld();
        CompletableFuture.runAsync(() -> {
            final PlayerRef playerRef = store.getComponent(reference, PlayerRef.getComponentType());
            if (playerRef == null)   return;

            if (partyCache.hasParty(playerRef.getUuid())) {
                playerRef.sendMessage(Message.raw("Você já está em uma party.").color(Color.RED));
                return;
            }

            final PlayerRef targetRef = commandContext.get(targetRefArg);
            if(targetRef == null) {
                playerRef.sendMessage(Message.raw("O jogador alvo não foi encontrado").color(Color.RED));
                return;
            }

            final Party party = partyCache.getByOwner(targetRef.getUuid());
            if(party == null) {
                playerRef.sendMessage(Message.raw("O jogador alvo não possui uma party.").color(Color.RED));
                return;
            }

            party.addMember(playerRef.getUuid());

            playerRef.sendMessage(Message.raw("Você entrou na party do jogador [" + targetRef.getUsername() + "]").color(Color.YELLOW));
            targetRef.sendMessage(Message.raw("O jogador [" + playerRef.getUsername() + "] entrou na sua party").color(Color.YELLOW));
        }, world);

        return CompletableFuture.completedFuture(null);
    }
}
