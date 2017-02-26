/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.iapi.data.interfaces;

import io.github.nucleuspowered.nucleus.Nucleus;
import io.github.nucleuspowered.nucleus.Util;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.util.Tuple;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public abstract class EndTimestamp {

    @Setting
    protected Long endtimestamp;

    @Setting
    protected Long timeFromNextLogin;

    /**
     * Gets the timestamp for the end of the mute.
     *
     * @return An {@link Instant}
     */
    public Optional<Instant> getEndTimestamp() {
        if (endtimestamp == null) {
            return Optional.empty();
        }

        return Optional.of(Instant.ofEpochSecond(endtimestamp));
    }

    public Optional<Duration> getTimeFromNextLogin() {
        if (timeFromNextLogin == null) {
            return Optional.empty();
        }

        return Optional.of(Duration.of(timeFromNextLogin, ChronoUnit.SECONDS));
    }

    public void setEndtimestamp(Instant time) {
        this.endtimestamp = time.getEpochSecond();
        this.timeFromNextLogin = null;
    }

    public void setTimeFromNextLogin(Duration duration) {
        this.timeFromNextLogin = duration.getSeconds();
        this.endtimestamp = null;
    }

    public void nextLoginToTimestamp() {
        if (timeFromNextLogin != null && endtimestamp == null) {
            endtimestamp = Instant.now().plus(timeFromNextLogin, ChronoUnit.SECONDS).getEpochSecond();
            timeFromNextLogin = null;
        }
    }

    public Optional<Duration> getTimeLeft() {
        if (endtimestamp == null && timeFromNextLogin == null) {
            return Optional.empty();
        }

        if (endtimestamp != null) {
            return Optional.of(Duration.between(Instant.ofEpochSecond(endtimestamp), Instant.now()));
        }

        return Optional.of(Duration.of(timeFromNextLogin, ChronoUnit.SECONDS));
    }

    // This HAS to be temporary.
    public Tuple<String, String> getForString() {
        if (getEndTimestamp().isPresent()) {
            return Tuple.of(Util.getTimeStringFromSeconds(Instant.now().until(getEndTimestamp().get(), ChronoUnit.SECONDS)),
                " " + Nucleus.getNucleus().getMessageProvider().getMessageWithFormat("standard.for") + " ");
        } else if (getTimeFromNextLogin().isPresent()) {
            return Tuple.of(Util.getTimeStringFromSeconds(getTimeFromNextLogin().get().getSeconds()),
                " " + Nucleus.getNucleus().getMessageProvider().getMessageWithFormat("standard.for") + " ");
        }

        return Tuple.of("", "");
    }
}
