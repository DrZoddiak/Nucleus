/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.mute.listeners;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.github.nucleuspowered.nucleus.api.module.mute.data.Mute;
import io.github.nucleuspowered.nucleus.modules.mute.config.MuteConfig;
import io.github.nucleuspowered.nucleus.modules.mute.services.MuteService;
import io.github.nucleuspowered.nucleus.core.scaffold.listener.ListenerBase;
import io.github.nucleuspowered.nucleus.core.services.INucleusServiceCollection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.LinearComponents;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.cause.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MuteCommandListener implements ListenerBase.Conditional {

    private final List<String> blockedCommands = new ArrayList<>();

    private final INucleusServiceCollection serviceCollection;
    private final MuteService handler;

    @Inject
    public MuteCommandListener(final INucleusServiceCollection serviceCollection) {
        this.serviceCollection = serviceCollection;
        this.handler = serviceCollection.getServiceUnchecked(MuteService.class);
    }

    @Listener(order = Order.FIRST)
    public void onPlayerSendCommand(final ExecuteCommandEvent.Pre event, @Root final ServerPlayer player) {
        if (!this.handler.isMuted(player.getUniqueId())) {
            return;
        }

        final String command = event.getCommand().toLowerCase();
        final Optional<? extends CommandMapping> oc = Sponge.getCommandManager().getCommandMapping(command);
        final Set<String> cmd;

        // If the command exists, then get all aliases.
        cmd = oc.map(commandMapping -> commandMapping.getAllAliases().stream().map(String::toLowerCase).collect(Collectors.toSet()))
                .orElseGet(() -> Sets.newHashSet(command));

        // If the command is in the list, block it.
        if (this.blockedCommands.stream().map(String::toLowerCase).anyMatch(cmd::contains)) {
            final Mute muteData = this.handler.getPlayerMuteInfo(player.getUniqueId()).orElse(null);
            if (muteData == null || muteData.expired()) {
                this.handler.unmutePlayer(player.getUniqueId());
            } else {
                this.handler.onMute(muteData, player);
                Sponge.getSystemSubject().sendMessage(LinearComponents.linear(
                        Component.text(player.getName() + "("),
                        this.serviceCollection.messageProvider().getMessage("standard.muted"),
                        Component.text("): "),
                        Component.text("/" + event.getCommand() + " " + event.getArguments())
                ));
                event.setCancelled(true);
            }
        }
    }

    // will also act as the reloadable.
    @Override public boolean shouldEnable(final INucleusServiceCollection serviceCollection) {
        this.blockedCommands.clear();
        this.blockedCommands.addAll(serviceCollection.configProvider().getModuleConfig(MuteConfig.class).getBlockedCommands());
        return !this.blockedCommands.isEmpty();
    }
}
