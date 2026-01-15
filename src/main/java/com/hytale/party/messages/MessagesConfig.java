package com.hytale.party.messages;

import com.hypixel.hytale.server.core.Message;
import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public class MessagesConfig {

    public static final Message ONLY_PLAYER = Message.translation("commands.errors.party.only-player").color(Color.RED);

    public static final Message ALREADY_IN_A_PARTY = Message.translation("commands.errors.party.already-in-a-party").color(Color.RED);
    public static final Message NOT_IN_A_PARTY = Message.translation("commands.errors.party.not-in-a-party").color(Color.RED);

    public static final Message ONLY_OWNER_CAN_INVITE = Message.translation("commands.errors.party.only-owner-can-invite").color(Color.RED);
    public static final Message ONLY_OWNER_CAN_DISBAND = Message.translation("commands.errors.party.only-owner-can-disband").color(Color.RED);
    public static final Message OWNER_CANT_LEAVE_PARTY = Message.translation("commands.errors.party.leader-cant-leave-party").color(Color.RED);

    public static final Message PLAYER_NOT_FOUND = Message.translation("commands.errors.party.player-not-found").color(Color.RED);
    public static final Message PLAYER_DONT_HAVE_PARTY = Message.translation("commands.errors.party.player-dont-have-party").color(Color.RED);

    public static final Message PLAYER_DONT_HAVE_INVITE= Message.translation("commands.errors.party.player-dont-have-invite").color(Color.RED);

    public static final Message PARTY_CREATED = Message.translation("commands.party.party-created").color(Color.YELLOW);
    public static final Message PARTY_DISBAND = Message.translation("commands.party.party-disband").color(Color.YELLOW);

    public static final Message PARTY_INVITE_SENT = Message.translation("commands.party.party-invite-sent").color(Color.YELLOW);
    public static final Message PARTY_INVITE_RECEIVED = Message.translation("commands.party.party-invite-received").color(Color.YELLOW);

    public static final Message PARTY_PUBLIC_STATUS = Message.translation("commands.party.party-public-status").color(Color.YELLOW);

    public static final Message PLAYER_JOIN_PARTY = Message.translation("commands.party.player-join-party").color(Color.YELLOW);
    public static final Message PLAYER_LEAVE_PARTY = Message.translation("commands.party.player-leave-party").color(Color.YELLOW);

    public static final Message PLAYER_JOIN_PARTY_TITLE = Message.translation("commands.party.title.player-join-party").color(Color.WHITE);
    public static final Message PLAYER_LEAVE_PARTY_TITLE = Message.translation("commands.party.title.player-leave-party").color(Color.WHITE);
}
