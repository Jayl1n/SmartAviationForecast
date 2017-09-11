package edu.cap.utils;

import java.io.File;

/**
 * Created by Jaylin on 17-7-3.
 */
public class ResourceUtils {
    public static File getFile(String name) {
        String file = ResourceUtils.class.getClassLoader().getResource(name).getFile();
        if (!new File(file).exists()) {
            file = System.getProperty("user.dir") + File.separator + name;
        }

        return new File(file);
    }
}
