/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.item.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ItemConfig {

    @Setting("repair")
    private RepairConfig repairConfig = new RepairConfig();

    @Setting("skull")
    private SkullConfig skullConfig = new SkullConfig();

    public RepairConfig getRepairConfig() {
        return this.repairConfig;
    }

    public SkullConfig getSkullConfig() {
        return this.skullConfig;
    }
}
