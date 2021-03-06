/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.commandspy.config;

import io.github.nucleuspowered.nucleus.core.services.interfaces.annotation.configuratehelper.LocalisedComment;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class CommandSpyConfig {

    @Setting("prefix")
    @LocalisedComment("config.commandspy.template")
    private String prefix = "&7[CS: {{name}}]: ";

    // use-whitelist
    @Setting(value = "filter-is-whitelist")
    @LocalisedComment("config.commandspy.usewhitelist")
    private boolean useWhitelist = true;

    // Was whitelisted-commands-to-spy-on
    // Removes the first "/" if it exists.
    // TODO: Remove the first "/"
    @Setting("command-filter")
    @LocalisedComment("config.commandspy.filter")
    private List<String> commands = new ArrayList<>();

    public String getTemplate() {
        return this.prefix;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public boolean isUseWhitelist() {
        return this.useWhitelist;
    }
}
