/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.core.services.impl.messageprovider.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * From https://gist.github.com/DemkaAge/8999236 with thanks.
 */
public class UTF8Control extends ResourceBundle.Control {

    public static final UTF8Control INSTANCE = new UTF8Control();

    private UTF8Control() {}

    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload)
            throws IOException {

        // The below is a copy of the default implementation.
        final String bundleName = this.toBundleName(baseName, locale);
        final String resourceName = this.toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;
        if (reload) {
            final URL url = loader.getResource(resourceName);
            if (url != null) {
                final URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try {
                // Only this line is changed to make it to read properties files as UTF-8.
                bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
            } finally {
                stream.close();
            }
        }
        return bundle;
    }
}