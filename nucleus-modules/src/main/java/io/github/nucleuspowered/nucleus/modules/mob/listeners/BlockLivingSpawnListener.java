/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.mob.listeners;

import io.github.nucleuspowered.nucleus.core.Registry;
import io.github.nucleuspowered.nucleus.modules.mob.config.BlockSpawnsConfig;
import io.github.nucleuspowered.nucleus.modules.mob.config.MobConfig;
import io.github.nucleuspowered.nucleus.core.scaffold.listener.ListenerBase;
import io.github.nucleuspowered.nucleus.core.services.INucleusServiceCollection;
import io.github.nucleuspowered.nucleus.core.services.interfaces.IReloadableService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlockLivingSpawnListener implements IReloadableService.Reloadable, ListenerBase.Conditional {

    private MobConfig config = new MobConfig();

    @Listener
    public void onSpawn(final SpawnEntityEvent event) {
        event.filterEntities(x -> {
            final Class<? extends Entity> entityType = x.getClass();
            return this.checkIsValid(entityType) || this.isSpawnable(x.type(), x.serverLocation().world());
        });
    }

    // Checks to see if the entity is of a type that should spawn regardless
    private boolean checkIsValid(final Class<? extends Entity> entityType) {
        return !Living.class.isAssignableFrom(entityType) || Player.class.isAssignableFrom(entityType) ||
                ArmorStand.class.isAssignableFrom(entityType);
    }

    private boolean isSpawnable(final EntityType<?> type, final ServerWorld world) {
        final Optional<BlockSpawnsConfig> bsco = this.config.getBlockSpawnsConfigForWorld(world);
        return bsco.flatMap(blockSpawnsConfig -> RegistryTypes.ENTITY_TYPE.get().findValueKey(type).map(x -> {
            final String id = x.asString().toLowerCase();
            return !(blockSpawnsConfig.isBlockVanillaMobs() && id.startsWith("minecraft:") || blockSpawnsConfig.getIdsToBlock().contains(id));
        })).orElse(true);

    }

    @Override
    public void onReload(final INucleusServiceCollection serviceCollection) {
        this.config = serviceCollection.configProvider().getModuleConfig(MobConfig.class);
    }

    @Override
    public boolean shouldEnable(final INucleusServiceCollection serviceCollection) {
            final Map<String, BlockSpawnsConfig> conf = serviceCollection.configProvider().getModuleConfig(MobConfig.class).getBlockSpawnsConfig();
            if (conf.entrySet().stream().anyMatch(x -> Sponge.server().worldManager().world(ResourceKey.resolve(x.getKey().toLowerCase())).isPresent())) {
                for (final BlockSpawnsConfig s : conf.values()) {
                    final List<String> idsToBlock = s.getIdsToBlock();
                    if (s.isBlockVanillaMobs() || RegistryTypes.ENTITY_TYPE.get().streamEntries().anyMatch(x -> idsToBlock.contains(x.key().asString()))) {
                        return true;
                    }
                }
            }

            return false;
    }
}
