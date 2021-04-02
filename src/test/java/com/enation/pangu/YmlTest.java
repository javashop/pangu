package com.enation.pangu;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/25
 */

public class YmlTest {

    @Test
    public void test() throws IOException {
        File file  = new File("/Users/kingapex/ideaworkspace/pangu/src/main/resources/executor/git_clone.yml");
//        String script = FileUtils.readFileToString(file, "utf-8");
        Yaml yaml = new Yaml();
        Map map = yaml.load(new FileInputStream(file));

        System.out.println(map);
    }
}
