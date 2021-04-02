package com.enation.pangu.utils;

import com.enation.pangu.service.impl.ConfigFileManagerImpl;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2021/1/18
 */

public abstract class PathUtil {
    private PathUtil() {

    }

    /**
     * 获取项目根目录
     * 目录规则为：运行的Jar包所在目录/pangu
     * @return
     */
    public static String getRootPath() {
        ApplicationHome home = new ApplicationHome(ConfigFileManagerImpl.class);
        File jarFile = home.getSource();
        String path = jarFile.getParentFile().toString();

        return path;
    }
}
