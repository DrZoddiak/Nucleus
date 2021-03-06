/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.core.services.impl.storage.dataobjects.configurate;

import org.spongepowered.configurate.ConfigurationNode;

/**
 * Indicates that this is the basic object for saving data with
 */
public abstract class AbstractConfigurateBackedDataObject implements IConfigurateBackedDataObject {

    protected ConfigurationNode backingNode;

    @Override public ConfigurationNode getBackingNode() {
        return this.backingNode.copy();
    }

    @Override public void setBackingNode(final ConfigurationNode node) {
        this.backingNode = node;
    }

}
