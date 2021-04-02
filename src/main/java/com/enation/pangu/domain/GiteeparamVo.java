package com.enation.pangu.domain;

/**
 * git仓库参数vo
 * @author zhanghao
 * @date 2020-11-15
 */
public class GiteeparamVo {

    /**
     * 类型
     * file 文件
     * dir 文件夹
     */
    private String type;

    /**
     * 文件或文件名
     */
    private String name;

    /**
     * 文件所在路径
     */
    private String path;

    /**
     * 文件下载路径
     */
    private String download_url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
