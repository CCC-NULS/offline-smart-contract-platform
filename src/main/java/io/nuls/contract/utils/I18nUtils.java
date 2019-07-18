package io.nuls.contract.utils;

import com.google.common.io.Resources;
import io.nuls.contract.constant.ToolsConstant;
import io.nuls.core.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class I18nUtils {
    private static final Logger logger = LoggerFactory.getLogger(I18nUtils.class);
    /**
     * 语言包池、内含所有配置的语言包数据
     * The language pool contains all the configured language package entity.
     */
    private static final Map<String, Properties> ALL_MAPPING = new HashMap<>();

    /**
     * 系统当前已选择语言包
     * The system currently selects the language package.
     */
    private static Properties nowMapping = null;

    /**
     * 默认语言设置为英文
     * default language is English
     */
    private static String key = "en";

    /**
     * 默认语言包存放文件夹名称
     * default properties file folder
     */
    private static final String FOLDER = "common-languages";

    public static boolean isSystemWin() {
        String pathSeparator = System.getProperty("path.separator");
        String unixPathSeparator = ":";
        if (unixPathSeparator.equals(pathSeparator)) {
            return false;
        } else {
            return true;
        }

    }

    public static void loadCommonLanguage(String defaultLanguage) {
        ALL_MAPPING.clear();
        load(I18nUtils.class, "common-languages", defaultLanguage);
    }

    public static void loadLanguage(Class c,String folder, String defaultLanguage) {
        ALL_MAPPING.clear();
        load(I18nUtils.class, "common-languages", defaultLanguage);
        load(c, folder, defaultLanguage);
    }

    /**
     * @param c
     * @param folder
     * @param defaultLanguage
     */
    private static void load(Class c, String folder, String defaultLanguage) {
        try {
            if (StringUtils.isBlank(folder)) {
                folder = FOLDER;
            }
            if (StringUtils.isNotBlank(defaultLanguage)) {
                key = defaultLanguage;
            }
            URL furl = Resources.getResource(folder);
            if (null != furl) {
                File folderFile = new File(furl.getPath());
                logger.info("furl.getPath()=" + furl.getPath());
                if (null != folderFile && null != folderFile.listFiles()) {
                    try {
                        for (File file : folderFile.listFiles()) {
                            Log.info("--------4----------" + file.getName());
                            InputStream is = new FileInputStream(file);
                            Properties prop = new Properties();
                            prop.load(new InputStreamReader(is, ToolsConstant.DEFAULT_ENCODING));
                            String key = file.getName().replace(".properties", "");
                            if (ALL_MAPPING.containsKey(key)) {
                                ALL_MAPPING.get(key).putAll(prop);
                            } else {
                                ALL_MAPPING.put(key, prop);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    URL url = c.getProtectionDomain().getCodeSource().getLocation();
                    Log.info("--------1----------");
                    Log.info( "url.getFile=" +  url.getFile()+" -----  "+url.getPath());
                    Log.info("--------2---------");
                    Log.info( "url.getPath()=" +  url.getPath());
                    Log.info("--------3---------");
                    if (url.getPath().endsWith(".jar")) {
                        Log.info( "url.getPath()=" +  url.getPath());
                        try {
                            JarFile jarFile = new JarFile(url.getFile());
                            Enumeration<JarEntry> entrys = jarFile.entries();
                            while (entrys.hasMoreElements()) {
                                JarEntry jar = entrys.nextElement();
                                Log.info( "jar.getName()=" +  jar.getName());
                                if (jar.getName().indexOf(folder + "/") == 0 && jar.getName().length() > (folder + "/").length()) {
                                    logger.info(jar.getName());
                                    InputStream in = I18nUtils.class.getClassLoader().getResourceAsStream(jar.getName());
                                    Properties prop = new Properties();
                                    prop.load(in);
                                    String key = jar.getName().replace(".properties", "");
                                    key = key.replace(folder + "/", "");
                                    logger.info("key={}", key);
                                    if (ALL_MAPPING.containsKey(key)) {
                                        ALL_MAPPING.get(key).putAll(prop);
                                    } else {
                                        ALL_MAPPING.put(key, prop);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        String jarPath=url.getPath();
                        Log.info( "jarPath=" +  jarPath);
                        if(url.getPath().indexOf("!")>0){
                            jarPath=url.getPath().substring(0,url.getPath().indexOf("!"));
                        }
                        URL newUrl=new URL(jarPath);
                        Log.info( "newUrl.getFile=" + newUrl.getPath());
                        if(newUrl.getPath().endsWith(".jar")){
                            //springboot jar包的资源加载
                            try {
                                JarFile jarFile = new JarFile(newUrl.getFile());
                                Enumeration<JarEntry> entrys = jarFile.entries();
                                while (entrys.hasMoreElements()) {
                                    JarEntry jar = entrys.nextElement();
                                    if(jar.getName().indexOf("languages" + "/")>0&&jar.getName().endsWith(".properties")){
                                        Log.info(jar.getName());
                                        InputStream in = I18nUtils.class.getClassLoader().getResourceAsStream(jar.getName());
                                        Properties prop = new Properties();
                                        prop.load(in);
                                        String key = jar.getName().replace(".properties", "");
                                        key = key.replace("BOOT-INF/classes/"+folder + "/", "");
                                        Log.info("key={}", key);
                                        if (ALL_MAPPING.containsKey(key)) {
                                            ALL_MAPPING.get(key).putAll(prop);
                                        } else {
                                            ALL_MAPPING.put(key, prop);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.error(e.getMessage());
                            }
                        } else {
                            logger.error("unSupport loadLanguage!");
                        }

                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * 设置系统语言，切换语言包
     * Set up the system language and switch the language package.
     *
     * @param lang 语言标识/Language identification
     */
    public static void setLanguage(String lang) throws Exception {
        if (StringUtils.isBlank(lang)) {
            throw new Exception(new Error());
        }
        key = lang;
        nowMapping = ALL_MAPPING.get(lang);
        if(nowMapping == null){
            throw new IllegalArgumentException("config error, can't found language package : " + lang);
        }
    }

    /**
     * 根据信息编码获取一条翻译后的消息体
     * Obtain a translated message body based on the information encoding.
     *
     * @param id 信息编码
     * @return String 翻译后的字符串/The translated string.
     */
    public static String get(String id) {
        if (nowMapping == null) {
            nowMapping = ALL_MAPPING.get(key);
        }
        return nowMapping.getProperty(id + "");
    }

    /**
     * 判断是否已加载某个语言包
     * Determines whether a language package has been loaded.
     *
     * @param lang 语言标识/Language identification
     * @return 判断结果/Determine the results
     */
    public static boolean hasLanguage(String lang) {
        Objects.requireNonNull(lang,"must be enter language");
        return ALL_MAPPING.containsKey(lang);
    }

    /**
     * 获取当前系统已设置的语言标识
     * Gets the language id that the current system has set.
     *
     * @return String 语言标识/Language identification
     */
    public static String getLanguage() {
        return key;
    }

    public static Map<String, Properties> getAll() {
        return ALL_MAPPING;
    }

}
