/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.nameban;

import io.github.nucleuspowered.nucleus.core.services.interfaces.annotation.PermissionMetadata;
import io.github.nucleuspowered.nucleus.core.services.interfaces.data.SuggestedLevel;

public final class NameBanPermissions {
    private NameBanPermissions() {
        throw new AssertionError("Nope");
    }

    @PermissionMetadata(descriptionKey = "permission.base", replacements = { "nameban" }, level = SuggestedLevel.ADMIN)
    public static final String BASE_NAMEBAN = "nucleus.nameban.base";

    @PermissionMetadata(descriptionKey = "permission.base", replacements = { "nameunban" }, level = SuggestedLevel.ADMIN)
    public static final String BASE_NAMEUNBAN = "nucleus.nameban.unban.base";

}
