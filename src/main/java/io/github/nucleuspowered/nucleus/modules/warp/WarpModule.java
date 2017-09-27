/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.warp;

import io.github.nucleuspowered.nucleus.Nucleus;
import io.github.nucleuspowered.nucleus.api.service.NucleusWarpService;
import io.github.nucleuspowered.nucleus.internal.qsml.module.ConfigurableModule;
import io.github.nucleuspowered.nucleus.modules.warp.config.WarpConfigAdapter;
import io.github.nucleuspowered.nucleus.modules.warp.handlers.WarpHandler;
import org.spongepowered.api.Sponge;
import uk.co.drnaylor.quickstart.annotations.ModuleData;

@ModuleData(id = "warp", name = "Warp")
public class WarpModule extends ConfigurableModule<WarpConfigAdapter> {

    @Override
    protected void performPreTasks() throws Exception {
        super.performPreTasks();

        try {
            // Put the warp service into the service manager.

            WarpHandler wh = new WarpHandler();
            serviceManager.registerService(WarpHandler.class, wh);
            Sponge.getServiceManager().setProvider(plugin, NucleusWarpService.class, wh);
        } catch (Exception ex) {
            Nucleus.getNucleus().getLogger().warn("Could not load the warp module for the reason below.");
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public WarpConfigAdapter createAdapter() {
        return new WarpConfigAdapter();
    }
}
