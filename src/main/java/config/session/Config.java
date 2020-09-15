package config.session;


import utils.CustomLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

/**
 * Read configuration and provide it by methods.
 */
public final class Config {

    private static final Properties CONFIG_PROPERTIES;

    private static final int SECOND = 1000;

    static {
        CONFIG_PROPERTIES = new Properties();
        try {
            URL url=ClassLoader.getSystemClassLoader().getResource("config.properties");
            CONFIG_PROPERTIES.load(Objects.requireNonNull(url).openStream());
        } catch (NullPointerException | IOException e) {
            CustomLogger.fail("Unable to load configuration file", e);
            throw new IllegalStateException("Unable to load configuration file", e);
        }
    }

    private Config() {
    }

    /**
     * Hub url getter from project configuration.
     *
     * @return configured hub url
     */
    public static URL getHubUrl() {
        try {
            return new URL(CONFIG_PROPERTIES.getProperty("selenium.hub.url"));
        } catch (MalformedURLException e) {
            CustomLogger.fail("Hub URL in configuration file is malformed");
            throw new IllegalStateException(e);
        }
    }

    /**
     * Global (default) timeout getter from project configuration.
     *
     * @return configured global timeout
     */
    public static int getGlobalTimeout() {
        return Integer.parseInt(CONFIG_PROPERTIES.getProperty("timeout.global"));
    }

    /**
     * Command waiting timeout (by driver) getter from project configuration. See appium DesiredCapabilities for details
     *
     * @return configured command timeout
     */
    public static int getCommandTimeout() {
        return Integer.parseInt(CONFIG_PROPERTIES.getProperty("timeout.command"));
    }

    /**
     * Animation duration getter from project configuration.
     *
     * @return configured animation duration
     */
    public static int getAnimationTimeout() {
        return Integer.parseInt(CONFIG_PROPERTIES.getProperty("timeout.animation")) * SECOND;
    }

    /**
     * Timeout for loading mobile_dimension.pages getter from project configuration.
     *
     * @return configured timeout fot page loading (in seconds)
     */
    public static int getPageLoadingTimeout() {
        return Integer.parseInt(CONFIG_PROPERTIES.getProperty("timeout.page.load"));
    }

    /**
     * Determines if wait strategy is 'fast' or not.
     *
     * @return true if wait.strategy from configuration is 'fast'
     */
    public static boolean getWaitStrategy() {
        return "fast".equals(CONFIG_PROPERTIES.getProperty("wait.strategy"));
    }

    /**
     * Determines if wait strategy is 'fast' or not.
     *
     * @return true if wait.strategy from configuration is 'fast'
     */
    public static String getGeneratedPagesLocation() {
        return CONFIG_PROPERTIES.getProperty("generated.pages.location");
    }

}
