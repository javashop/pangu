package com.enation.pangu;

import com.enation.pangu.service.CheckerManager;
import com.enation.pangu.service.ExecutorManager;
import com.enation.pangu.service.impl.CheckerManagerImpl;
import com.enation.pangu.service.impl.ExecutorManagerImpl;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.File;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ComponentScan("com.enation.pangu")
public class RepositoryTest {

    //获取项目根目录
    @Test
    public void test() {
        System.getProperty("user.dir");
        System.out.println("项目根目录" + System.getProperty("user.dir"));
    }


    //读取yml文件中检测器
    @Test
    public void test2() {
        CheckerManager checkerManager = new CheckerManagerImpl();
        checkerManager.list();
    }

    //根据url下载文件
    @Test
    public void test3() {
        //System.out.println("\\\\");
        String res =
                downloadFromUrl("https://gitee.com/zhanghao666720/javashop/raw/master/application-dev.yml",
                        "C:\\Users\\Administrator\\Desktop\\耶巴蒂\\莱维贝贝\\图片\\测试下载文件\\");
        System.out.println(res);
    }

    public static String downloadFromUrl(String url, String dir) {
        try {
            URL httpurl = new URL(url);
            String fileName = getFileNameFromUrl(url);
            System.out.println(fileName);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }

    public static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }

}
