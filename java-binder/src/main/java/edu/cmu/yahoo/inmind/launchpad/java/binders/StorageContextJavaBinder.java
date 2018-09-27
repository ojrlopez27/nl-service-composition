package edu.cmu.yahoo.inmind.launchpad.java.binders;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.cmu.yahoo.inmind.launchpad.utils.LaunchpadContext;
import edu.cmu.yahoo.inmind.launchpad.utils.LoggerContext;
import edu.cmu.yahoo.inmind.launchpad.utils.StorageContext;

/**
 * Created by dangiankit on 2/14/18.
 */

public class StorageContextJavaBinder extends StorageContext {

    private static final String STORAGE_LOCATION_ROOT = System.getProperty("user.home") + File.separator + "OSGi-Launchpad";
    private static final String STORAGE_LOCATION_FELIX_CACHE = STORAGE_LOCATION_ROOT + File.separator;

    private static String TAG = StorageContextJavaBinder.class.getSimpleName();
    private static LoggerContext logger = LaunchpadContext.getLoggerContext();
    private static StorageContextJavaBinder storageContext;

    private StorageContextJavaBinder() {

        // create a storage location for the OSGi launchpad
        new File(STORAGE_LOCATION_ROOT).mkdirs();
    }

    public static StorageContext getInstance() {
        if (storageContext == null) {
            storageContext = new StorageContextJavaBinder();
        }
        return storageContext;
    }

    @Override
    public File createCacheDir(String cacheDirLocation, boolean refresh) throws IOException {

        logger.d(TAG, "Creating Cache Dir");
        File cacheDir = new File(STORAGE_LOCATION_FELIX_CACHE + cacheDirLocation);

        if (refresh && cacheDir.exists()) {
            Files.walk(Paths.get(STORAGE_LOCATION_FELIX_CACHE + cacheDirLocation))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        }
        cacheDir.mkdirs();

        return isDirCreated(cacheDir, refresh);
    }

    @Override
    public boolean isExternalStorageWritable() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isExternalStorageReadable() {
        return Boolean.TRUE;
    }

}
