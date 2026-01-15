package com.hytale.party.compass;

import com.hypixel.hytale.builtin.blocktick.BlockTickPlugin;
import com.hypixel.hytale.builtin.fluid.FluidPlugin;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.asset.type.gameplay.GameplayConfig;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.util.PositionUtil;
import com.hytale.party.PartyPlugin;
import com.hytale.party.cache.PartyCache;
import com.hytale.party.model.Party;

import java.util.List;

public class PartyMemberMarkerProvider implements WorldMapManager.MarkerProvider {

    public static final PartyMemberMarkerProvider INSTANCE = new PartyMemberMarkerProvider();

    private PartyMemberMarkerProvider() {

    }

    @Override
    public void update(World world, GameplayConfig gameplayConfig, WorldMapTracker worldMapTracker, int viewRadius, int playerChunkX, int playerChunkZ) {
        final Player player = worldMapTracker.getPlayer();

        final EntityStore entityStore = world.getEntityStore();
        final Store<EntityStore> store = entityStore.getStore();

        final Ref<EntityStore> reference = player.getReference();
        if (reference == null) {
            return;
        }

        world.execute(() -> {
            final PlayerRef playerRef = store.getComponent(reference, PlayerRef.getComponentType());
            if (playerRef == null || !playerRef.isValid()) {
                return;
            }

            final PartyCache partyCache = PartyPlugin.getInstance().getPartyCache();
            final Party party = partyCache.getParty(playerRef.getUuid());
            if (party == null || party.getMembers().size() == 1) return;

            final List<PlayerRef> players = party.getPlayers();
            for (int index = 0; index < players.size(); index++) {
                final PlayerRef partyMember = players.get(index);
                if (partyMember.getWorldUuid() == null ||
                        !partyMember.getWorldUuid().equals(playerRef.getWorldUuid())) {
                    continue;
                }

                final Transform transform = partyMember.getTransform();
                final Vector3d position = transform.getPosition();

                final String finalIconName = "CustomPlayer-" + index + ".png";
                worldMapTracker.trySendMarker(
                        -1,
                        playerChunkX,
                        playerChunkZ,
                        position,
                        0.0f,
                        "PartyMember-" + partyMember.getUuid(),
                        partyMember.getUsername(),
                        partyMember,
                        (id, name, ref) -> new MapMarker(
                                id, name, finalIconName, PositionUtil.toTransformPacket(ref.getTransform()), null));

                System.out.println("sent marker");
            }
        });
    }
}
