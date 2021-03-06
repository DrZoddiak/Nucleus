/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.api.module.rtp;

import io.github.nucleuspowered.nucleus.api.module.rtp.kernel.RTPKernel;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.biome.Biome;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;
import java.util.Set;

/**
 * Allows for the customisation of RTP selection routines.
 */
public interface NucleusRTPService {

    /**
     * Gets the {@link RTPOptions} that contains the current
     * rtp configuration for a default world.
     *
     * @return The {@link RTPOptions}
     */
    default RTPOptions options() {
        return this.options(null);
    }

    /**
     * Gets the {@link RTPOptions} that contains the current
     * rtp configuration for the specified world.
     *
     * @param world The {@link ServerWorldProperties}, or {@code null} for defaults.
     * @return The {@link RTPOptions}
     */
    RTPOptions options(@Nullable ServerWorldProperties world);

    /**
     * Gets the {@link RTPOptions} that contains the current
     * rtp configuration, but in builder format.
     *
     * <p>Changes in the builder do not affect the configuration,
     * this is for overriding in specific circumstances.</p>
     *
     * @return The {@link RTPOptions}
     */
    RTPOptions.Builder optionsBuilder();

    /**
     * Gets the {@link RTPKernel} that Nucleus' RTP will use.
     *
     * <p>This is set by the rtp config, using the kernel's ID.</p>
     *
     * @return The {@link RTPKernel}
     */
    RTPKernel getDefaultKernel();

    /**
     * Uses the default RTP kernel (from {@link #getDefaultKernel()} to find a
     * random location, given the provided {@link RTPOptions}.
     *
     * <p>Note that this may fail if the search times out. This does not
     * indicate a failing routine.</p>
     *
     * <p>A source of confusion is that this method works in exactly the same
     * way as the rtp command. <strong>It does not.</strong> The RTP command
     * makes attempts over multiple ticks to keep the server running while
     * increasing the chance of finding a suitable spot.</p>
     *
     * <p>This method makes <strong>one</strong> attempt at finding a
     * suitable location. You may need to run this more than once.</p>
     *
     * @param currentLocation The current location to base the RTP off.
     * @param world The world to warp to
     * @param options The RTP options to use
     * @return The location if one was found.
     */
    default Optional<ServerLocation> getLocation(@Nullable final ServerLocation currentLocation, final ServerWorld world, final RTPOptions options) {
        return this.getDefaultKernel().getLocation(currentLocation, world, options);
    }

    /**
     * Gets the default {@link RTPKernel} used when executing
     * a random teleport
     *
     * @param world The world to get the kernel for
     * @return The kernel
     */
    RTPKernel getKernel(ServerWorldProperties world);

    RTPKernel getKernel(ServerWorld world);

    /**
     * Gets the default {@link RTPKernel} used when executing
     * a random teleport
     *
     * @param world The name of the world to get the kernel for
     * @return The kernel
     */
    RTPKernel getKernel(String world);

    /**
     * The RTP options to pass to the routines
     */
    interface RTPOptions {

        /**
         * The maximum radius for the RTP.
         *
         * @return the max radius
         */
        int maxRadius();

        /**
         * The minimum radius for the RTP.
         *
         * @return the min radius
         */
        int minRadius();

        /**
         * The minimum height for the RTP. Must be greater than zero.
         *
         * @return The minimum height
         */
        int minHeight();

        /**
         * The maximum height for the RTP. Must be greater than the min height.
         *
         * @return The maximum height.
         */
        int maxHeight();

        /**
         * A set of biomes that RTP should not teleport a player into.
         *
         * @return The set of biomes
         */
        Set<Biome> prohibitedBiomes();

        interface Builder {

            /**
             * Sets the maximum radius for the RTP.
             *
             * @param max the max radius
             * @return This builder, for chaining
             */
            Builder maxRadius(int max);

            /**
             * Sets the minimum radius for the RTP.
             *
             * @param min the min radius
             * @return This builder, for chaining
             */
            Builder minRadius(int min);

            /**
             * Sets the minimum height for the RTP. Must be greater than zero.
             *
             * @param max the minimum height
             * @throws IllegalArgumentException if the height is not acceptable
             * @return This builder, for chaining
             */
            Builder minHeight(int max) throws IllegalArgumentException;

            /**
             * Sets the maximum height for the RTP. Must be greater than zero.
             *
             * @param min the max height
             * @throws IllegalArgumentException if the height to too high
             * @return This builder, for chaining
             */
            Builder maxHeight(int min) throws IllegalArgumentException;

            /**
             * Adds a {@link Biome} to the prohibited biomes set.
             *
             * @param biomeType The {@link Biome} to add.
             * @return This builder, for chaining.
             */
            Builder prohibitedBiome(Biome biomeType);

            /**
             * Sets this builder state from the specified {@link RTPOptions}
             * @param options The optiosn
             * @return This builder, for chaining
             */
            Builder from(RTPOptions options);

            /**
             * Creates a {@link RTPOptions} from the state of this builder.
             *
             * @throws IllegalStateException if the builder is not in the correct state
             * @return The {@link RTPOptions}
             */
            RTPOptions build() throws IllegalStateException;

            /**
             * Resets this builder to the default state.
             * @return This builder, for chaining
             */
            Builder reset();
        }
    }
}
