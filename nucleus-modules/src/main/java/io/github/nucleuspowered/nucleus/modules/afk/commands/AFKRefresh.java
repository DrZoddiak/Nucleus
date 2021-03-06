/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.afk.commands;

import io.github.nucleuspowered.nucleus.modules.afk.AFKPermissions;
import io.github.nucleuspowered.nucleus.modules.afk.services.AFKHandler;
import io.github.nucleuspowered.nucleus.core.scaffold.command.ICommandContext;
import io.github.nucleuspowered.nucleus.core.scaffold.command.ICommandExecutor;
import io.github.nucleuspowered.nucleus.core.scaffold.command.ICommandResult;
import io.github.nucleuspowered.nucleus.core.scaffold.command.annotation.Command;

@Command(aliases = "afkrefresh", commandDescriptionKey = "afkrefresh", basePermission = AFKPermissions.BASE_AFKREFRESH)
public class AFKRefresh implements ICommandExecutor {

    @Override public ICommandResult execute(final ICommandContext context) {
        context.getServiceCollection().getServiceUnchecked(AFKHandler.class).invalidateAfkCache();
        context.sendMessage("command.afkrefresh.complete");
        return context.successResult();
    }
}
