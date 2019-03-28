package edu.cmu.yahoo.inmind.launchpad.java.binders;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.cmu.yahoo.inmind.launchpad.core.framework.FelixFrameworkException;
import edu.cmu.yahoo.inmind.launchpad.utils.LaunchpadContext;
import edu.cmu.yahoo.inmind.launchpad.utils.PropertiesContext;
import edu.cmu.yahoo.inmind.launchpad.utils.PropertyManager;

/**
 * Created by dangiankit on 2/14/18.
 */

public class PropertiesContextJavaBinder implements PropertiesContext {
    private static final String TAG = PropertiesContextJavaBinder.class.getSimpleName();
    private static final String ERR_PROPS_FAILURE = "Failed to load properties file: ";

    // Properties file expected in the project's assets directory
    private static final String PROPS_LAUNCHPAD         = "launchpad.properties";
    private static final String PROPS_BUNDLEREPO        = "bundlerepo.properties";
    private static final String PROPS_SERVER_CONFIG     = "config.properties";

    // Property keys expected in the launchpad.properties file
    private static final String MAVEN2_AUTH_HTTP_USERNAME           = "launchpad.maven2.auth.username";
    private static final String MAVEN2_AUTH_HTTP_PASSWORD           = "launchpad.maven2.auth.password";
    private static final String URL_OSGI_REMOTE_SERVER              = "launchpad.osgi.remote.server";
    private static final String OSGI_CONFIG_AUTO_REFRESH            = "launchpad.osgi.config.auto.refresh";
    private static final String OSGI_STAGED_BUNDLES                 = "launchpad.osgi.staged.bundles";
    private static final String URL_REMOTE_LAUNCHPAD_CONF           = "launchpad.osgi.remote.conf";
    private static final String URL_REMOTE_LAUNCHPAD_STAGED         = "launchpad.osgi.remote.staged";
    private static final String URL_STAGED_LIBS                     = "launchpad.osgi.remote.staged.felix";
    private static final String ANDROID_FELIX_CACHE_LOCATION        = "launchpad.osgi.felix.cache.location";
    private static final String ANDROID_FELIX_BUNDLE_LOCATION       = "launchpad.osgi.felix.bundle.location";
    private static final String ANDROID_FELIX_STORAGE_AUTO_ERASE    = "launchpad.osgi.felix.storage.auto.erase";

    // property keys expected in config.properties file on the remote server: not provided elsewhere
    public static final String FELIX_AUTO_DEPLOY_ACTION             = "felix.auto.deploy.action";
    public static final String FELIX_EMBEDDED_EXECUTION             = "felix.embedded.execution";
    public static final String OSGI_SERVICE_HTTP_PORT               = "org.osgi.service.http.port";

    private static PropertiesContextJavaBinder propertiesContext;
    private static Properties launchpadProps;
    private static Properties bundlerepoProps;

    public static PropertiesContext getInstance() {
        if (propertiesContext == null) {
            propertiesContext = new PropertiesContextJavaBinder();
        }
        return propertiesContext;
    }

    private PropertiesContextJavaBinder() {
        if (launchpadProps == null) {
            launchpadProps = initProperties(PROPS_LAUNCHPAD);
        }
        if (bundlerepoProps == null) {
            bundlerepoProps = initProperties(PROPS_BUNDLEREPO);
        }
    }

    @Override
    public String getMavenAuthHttpUsername() {
        return launchpadProps.getProperty(MAVEN2_AUTH_HTTP_USERNAME);
    }

    @Override
    public String getMavenAuthHttpPassword() {
        return launchpadProps.getProperty(MAVEN2_AUTH_HTTP_PASSWORD);
    }

    @Override
    public String getOsgiRemoteServerURL() {
        return launchpadProps.getProperty(URL_OSGI_REMOTE_SERVER);
    }

    @Override
    public String getOsgiConfigAutoRefresh() {
        return launchpadProps.getProperty(OSGI_CONFIG_AUTO_REFRESH);
    }

    @Override
    public String getOsgiStagedBndles() {
        return launchpadProps.getProperty(OSGI_STAGED_BUNDLES);
    }

    @Override
    public String getFelixCacheDirLocation() {
        return launchpadProps.getProperty(ANDROID_FELIX_CACHE_LOCATION);
    }

    @Override
    public String getFelixBundleDirLocation() {
        return launchpadProps.getProperty(ANDROID_FELIX_BUNDLE_LOCATION);
    }

    @Override
    public boolean getFelixStorageAutoErase() {
        return Boolean.parseBoolean(launchpadProps.getProperty(ANDROID_FELIX_STORAGE_AUTO_ERASE));
    }

    @Override
    public String getFelixAutoDeployAction() { return FELIX_AUTO_DEPLOY_ACTION; }

    @Override
    public String getFelixEmbeddedExecution() { return FELIX_EMBEDDED_EXECUTION; }

    @Override
    public String getOsgiServiceHttpPort() { return OSGI_SERVICE_HTTP_PORT; }

    @Override
    public String getRemoteLaunchpadConf() {
        return launchpadProps.getProperty(URL_REMOTE_LAUNCHPAD_CONF);
    }

    @Override
    public String getRemoteLaunchpadStaged() {
        return launchpadProps.getProperty(URL_REMOTE_LAUNCHPAD_STAGED);
    }

    @Override
    public String getRemoteLaunchpadStagedLibs() {
        return launchpadProps.getProperty(URL_STAGED_LIBS);
    }

    @Override
    public String getRemoteLaunchpadConfProps() {
        return getRemoteLaunchpadConf() + File.separator + PROPS_SERVER_CONFIG;
    }

    @Override
    public Properties getLaunchpadProps() {
        return launchpadProps;
    }

    @Override
    public Properties getBundleRepositoryProps() {
        return bundlerepoProps;
    }

    private static Properties initProperties(String propertiesFile) {
        try {
            // open an input stream from the Android context assets location
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile);
            return PropertyManager.loadProperties(inputStream);
        }
        catch (IOException ioException) {
            LaunchpadContext.getLoggerContext().e(TAG, ioException);
            throw new FelixFrameworkException(ERR_PROPS_FAILURE + propertiesFile);
        }
    }
}
