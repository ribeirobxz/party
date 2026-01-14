package com.hytale.party.command.impl;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.collision.WorldUtil;
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

public class PartyCreateSubCommand extends AbstractAsyncCommand {

    private final PartyCache partyCache;

    public PartyCreateSubCommand(PartyCache partyCache) {
        super("criar", "Crie uma party para seus amigos.");
        this.partyCache = partyCache;
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
                commandContext.sendMessage(Message.raw("Você já está em uma party.").color(Color.RED));
                return;
            }

            final HashSet<UUID> members = new HashSet<>(Collections.singleton(playerRef.getUuid()));
            final Party party = new Party(playerRef.getUuid(), members);

            partyCache.add(party);
            playerRef.sendMessage(Message.raw("Você criou uma party com sucesso.").color(Color.YELLOW));
        }, world);

        return CompletableFuture.completedFuture(null);
    }
}
