package com.example.codegenerateor.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @Auther: feihu5
 * @Date: 2018/10/17 15:08
 * @Description:
 */
public class EnvUtil {
    private static final Logger logger = Logger.getLogger("EnvUtil");
    private static Boolean OS_LINUX = null;

    public EnvUtil() {
    }

    public static boolean isLinux() {
        if (OS_LINUX == null) {
            String OS = System.getProperty("os.name").toLowerCase();
            logger.info("os.name: " + OS);
            if (OS != null && OS.contains("windows")) {
                OS_LINUX = false;
            } else {
                OS_LINUX = true;
            }
        }

        return OS_LINUX;
    }

    public static Properties getEnv() {
        Properties prop = new Properties();

        try {
            Process p = null;
            if (isLinux()) {
                p = Runtime.getRuntime().exec("sh -c set");
            } else {
                p = Runtime.getRuntime().exec("cmd /c set");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while((line = br.readLine()) != null) {
                int i = line.indexOf("=");
                if (i > -1) {
                    String key = line.substring(0, i);
                    String value = line.substring(i + 1);
                    prop.setProperty(key, value);
                }
            }

            br.close();
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return prop;
    }
}
