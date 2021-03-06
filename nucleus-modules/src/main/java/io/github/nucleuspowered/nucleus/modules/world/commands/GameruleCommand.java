/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.world.commands;

import io.github.nucleuspowered.nucleus.core.Util;
import io.github.nucleuspowered.nucleus.modules.world.WorldPermissions;
import io.github.nucleuspowered.nucleus.core.scaffold.command.ICommandContext;
import io.github.nucleuspowered.nucleus.core.scaffold.command.ICommandExecutor;
import io.github.nucleuspowered.nucleus.core.scaffold.command.ICommandResult;
import io.github.nucleuspowered.nucleus.core.scaffold.command.NucleusParameters;
import io.github.nucleuspowered.nucleus.core.scaffold.command.annotation.Command;
import io.github.nucleuspowered.nucleus.core.services.INucleusServiceCollection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.gamerule.GameRule;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Command(
        aliases = {"gamerule"},
        basePermission = WorldPermissions.BASE_WORLD_GAMERULE,
        commandDescriptionKey = "world.gamerule",
        parentCommand = WorldCommand.class
)
public class GameruleCommand implements ICommandExecutor {

    @Override public Parameter[] parameters(final INucleusServiceCollection serviceCollection) {
        return new Parameter[] {
                NucleusParameters.ONLINE_WORLD_OPTIONAL
        };
    }

    @Override public ICommandResult execute(final ICommandContext context) throws CommandException {
        final ServerWorld serverWorld = context.getWorldPropertiesOrFromSelfOptional(NucleusParameters.ONLINE_WORLD.key())
                .orElseThrow(() -> context.createException("command.world.player"));
        final ServerWorldProperties worldProperties = serverWorld.properties();
        final Map<GameRule<?>, ?> gameRules = worldProperties.gameRules();

        final String message = context.getMessageString("command.world.gamerule.key");
        final List<Component> text = gameRules.entrySet().stream().sorted(Comparator.comparing(x -> x.getKey().name()))
            .map(x -> LegacyComponentSerializer.legacyAmpersand()
                            .deserialize(MessageFormat.format(message, x.getKey(), String.valueOf(x.getValue())))
                            .clickEvent(ClickEvent.suggestCommand(String.format("/nucleus:world gamerule set %s %s ",
                                    worldProperties.key().asString(),
                                    x.getKey()))))
                .collect(Collectors.toList());

        Util.getPaginationBuilder(context.audience())
            .title(context.getMessage("command.world.gamerule.header", worldProperties.key().asString()))
            .contents(text)
            .sendTo(context.audience());

        return context.successResult();
    }
}
