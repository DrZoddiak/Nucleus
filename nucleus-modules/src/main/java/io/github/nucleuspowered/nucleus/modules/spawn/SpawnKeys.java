/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.spawn;

import io.github.nucleuspowered.nucleus.core.util.GeAnTyRefTypeTokens;
import org.spongepowered.math.vector.Vector3d;
import io.github.nucleuspowered.nucleus.core.configurate.datatypes.LocationNode;
import io.github.nucleuspowered.nucleus.core.services.impl.storage.dataobjects.modular.IGeneralDataObject;
import io.github.nucleuspowered.nucleus.core.services.impl.storage.dataobjects.modular.IWorldDataObject;
import io.github.nucleuspowered.storage.dataobjects.keyed.DataKey;

public final class SpawnKeys {

    public static DataKey<LocationNode, IGeneralDataObject> FIRST_SPAWN_LOCATION = DataKey.of(
            GeAnTyRefTypeTokens.LOCATION_NODE,
            IGeneralDataObject.class,
            "firstSpawn");

    public static DataKey<Vector3d, IWorldDataObject> WORLD_SPAWN_ROTATION = DataKey.of(
            GeAnTyRefTypeTokens.VECTOR_3D,
            IWorldDataObject.class,
            "s[pawn-rotation");
}