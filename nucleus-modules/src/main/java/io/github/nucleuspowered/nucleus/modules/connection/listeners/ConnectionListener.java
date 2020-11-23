/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.connection.listeners;

import com.google.inject.Inject;
import io.github.nucleuspowered.nucleus.modules.connection.ConnectionPermissions;
import io.github.nucleuspowered.nucleus.modules.connection.config.ConnectionConfig;
import io.github.nucleuspowered.nucleus.core.scaffold.listener.ListenerBase;
import io.github.nucleuspowered.nucleus.core.services.INucleusServiceCollection;
import io.github.nucleuspowered.nucleus.core.services.interfaces.IPermissionService;
import io.github.nucleuspowered.nucleus.core.services.interfaces.IReloadableService;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.util.Tristate;

public class ConnectionListener implements IReloadableService.Reloadable, ListenerBase {

    private final IPermissionService permissionService;

    private int reservedSlots = 0;
    @Nullable private Component whitelistMessage;
    @Nullable private Component fullMessage;

    @Inject
    public ConnectionListener(final IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Perform connection events on when a player is currently not permitted to join.
     *
     * @param event The event.
     */
    @Listener(order = Order.FIRST)
    @IsCancelled(Tristate.TRUE)
    public void onPlayerJoinAndCancelled(final ServerSideConnectionEvent.Login event, @Getter("getUser") final User user) {
        // Don't affect the banned.
        final BanService banService = Sponge.getServer().getServiceProvider().banService();
        if (banService.isBanned(user.getProfile()) || banService.isBanned(event.getConnection().getAddress().getAddress())) {
            return;
        }

        if (Sponge.getServer().hasWhitelist()
            && !Sponge.getServer().getServiceProvider().whitelistService().isWhitelisted(user.getProfile())) {
            if (this.whitelistMessage != null) {
                event.setMessage(this.whitelistMessage);
                event.setCancelled(true);
            }

            // Do not continue, whitelist should always apply.
            return;
        }

        final int slotsLeft = Sponge.getServer().getMaxPlayers() - Sponge.getServer().getOnlinePlayers().size();
        if (slotsLeft <= 0) {
            if (this.permissionService.hasPermission(user, ConnectionPermissions.CONNECTION_JOINFULLSERVER)) {

                // That minus sign before slotsLeft is not a typo. Leave it be!
                // It will be negative, reserved slots is positive - need to account for that.
                if (this.reservedSlots <= -1 || -slotsLeft < this.reservedSlots) {
                    event.setCancelled(false);
                    return;
                }
            }

            if (this.fullMessage != null) {
                event.setMessage(this.fullMessage);
            }
        }

    }

    @Override
    public void onReload(final INucleusServiceCollection serviceCollection) {
        final ConnectionConfig connectionConfig = serviceCollection.configProvider().getModuleConfig(ConnectionConfig.class);
        this.reservedSlots = connectionConfig.getReservedSlots();
        this.whitelistMessage = connectionConfig.getWhitelistMessage().orElse(null);
        this.fullMessage = connectionConfig.getServerFullMessage().orElse(null);
    }

}
