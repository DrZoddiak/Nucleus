/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.rtp.options;

import io.github.nucleuspowered.nucleus.api.module.rtp.NucleusRTPService;
import io.github.nucleuspowered.nucleus.modules.rtp.config.RTPConfig;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.world.biome.Biome;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RTPOptions implements NucleusRTPService.RTPOptions {

    private final int min;
    private final int max;
    private final int minHeight;
    private final int maxHeight;
    private final Set<Biome> prohibitedBiomes;

    public RTPOptions(final RTPConfig config, @Nullable final String worldName) {
        this.min = config.getMinRadius(worldName);
        this.max = config.getRadius(worldName);
        this.maxHeight = config.getMaxY(worldName);
        this.minHeight = config.getMinY(worldName);
        this.prohibitedBiomes = Collections.unmodifiableSet(config.getProhibitedBiomes());
    }

    RTPOptions(final RTPOptionsBuilder builder) {
        this.min = builder.min;
        this.max = builder.max;
        this.minHeight = builder.minheight;
        this.maxHeight = builder.maxheight;
        this.prohibitedBiomes = Collections.unmodifiableSet(new HashSet<>(builder.prohibitedBiomes));
    }

    @Override
    public int maxRadius() {
        return this.max;
    }

    @Override
    public int minRadius() {
        return this.min;
    }

    @Override public int minHeight() {
        return this.minHeight;
    }

    @Override public int maxHeight() {
        return this.maxHeight;
    }

    @Override public Set<Biome> prohibitedBiomes() {
        return this.prohibitedBiomes;
    }
}
