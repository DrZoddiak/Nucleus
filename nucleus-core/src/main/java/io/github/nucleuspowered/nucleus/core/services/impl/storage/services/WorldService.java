/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.core.services.impl.storage.services;

import com.google.gson.JsonObject;
import io.github.nucleuspowered.nucleus.core.services.impl.storage.dataobjects.modular.IWorldDataObject;
import io.github.nucleuspowered.nucleus.core.services.impl.storage.queryobjects.IWorldQueryObject;
import io.github.nucleuspowered.nucleus.core.services.interfaces.IDataVersioning;
import io.github.nucleuspowered.nucleus.core.services.interfaces.IStorageManager;
import io.github.nucleuspowered.storage.services.AbstractKeyedService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;

import java.util.function.BiConsumer;

public class WorldService extends AbstractKeyedService<ResourceKey, IWorldQueryObject, IWorldDataObject, JsonObject> {

    public WorldService(final IStorageManager repository, final PluginContainer pluginContainer, final IDataVersioning dataVersioning) {
        super(repository::getWorldDataAccess, repository::getWorldRepository, dataVersioning::migrate, dataVersioning::setVersion, pluginContainer);
    }

    @Override
    protected void onEviction(final ResourceKey key, final IWorldDataObject dataObject, final BiConsumer<ResourceKey, IWorldDataObject> reAdd) {
        Sponge.server().worldManager().world(key).ifPresent(x -> reAdd.accept(key, dataObject));
    }

}
