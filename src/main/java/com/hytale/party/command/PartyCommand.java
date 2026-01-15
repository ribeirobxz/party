package com.hytale.party.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.command.impl.*;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class PartyCommand extends CommandBase {

    private final PartyCache partyCache;

    public PartyCommand(PartyCache partyCache) {
        super("party", "Acesse os comandos da party");
        this.partyCache = partyCache;

        addSubCommand(new PartyCreateSubCommand(partyCache));
        addSubCommand(new PartyJoinSubCommand(partyCache));
        addSubCommand(new PartyPublicSubCommand(partyCache));
        addSubCommand(new PartyInviteSubCommand(partyCache));
        addSubCommand(new PartyDisbandSubCommand(partyCache));
        addSubCommand(new PartyLeaveSubCommand(partyCache));
        addSubCommand(new PartyChatSubCommand(partyCache));
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        commandContext.sendMessage(Message.join(Message.raw(""), Message.raw(" /party create - Create the party"),
                Message.raw(" /party disband - Disband the party"),
                Message.raw(" /party leave - Leave the party"),
                Message.raw(" /party join <owner-name> - Join the party"),
                Message.raw(" /party invite <player-name> - Invite the player to your party"),
                Message.raw(" /party public - Turn your party public to others players")));
    }
}
