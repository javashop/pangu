# 示例

## 菜单激活

### 第一步：为菜单定义id:

~~~html
            <a href="#" id="m-machine" class="nav-link">
                <i class="nav-icon iconfont icon-server1"></i>
                <p>
                    机器
                </p>
            </a>
~~~

如上所示，id="m-machine"

### 第二步：激活菜单

在相应的页面调用激活菜单function:

~~~javascript
$(function () {
    //激活部署菜单
    activeMenu("m-deploy");

});

~~~

## 消息提示

### 成功消息

~~~javascript
Message.success('删除成功');
~~~

### 失败消息

~~~javascript
Message.error("请输入标签名称");
~~~

### 确认消息框

~~~javascript
 Message.confirm("确认删除这个标签吗？",function (){
    //这里是确认后的回调函数
 });
~~~



## ssh客户端调用示例

### 创建客户端

首先要创建客户端,可以通过两种方式创建客户端，基于密码认证，或基于证书认证

#### 密码认证

~~~
SshClient sshClient = SshClientFactory.createSsh("root", "752513", "192.168.2.55", 22);
~~~

### 证书认证

//todo

### 执行ssh 命令

~~~java
    @Test
    public void testExec() throws  IOException {
        SshClient sshClient = SshClientFactory.createSsh("root", "752513", "192.168.2.55", 22);
        int result = sshClient.exec("echo a;sleep 1;echo b; sleep 1;echo c", out -> {
            System.out.println(out);
        });
				sshClient.disconnect();
        System.out.println("命令执行结果是： "+result);
    }
~~~



exec方法第二个参数是一个回调器：

~~~java
public interface ExecCallback {


    /**
     * 回调执行方法
     * @param out 命令执行的输出
     */
    void callback(String out);
}
~~~

在命令执行过程中，没500毫秒，调一次这个方法

exec的返回值0表示命令执行成功，1为执行失败

### 文件传输

~~~java
    /**
     * scp 测试
     * @throws IOException
     */
    @Test
    public void testScp() throws  IOException {
        SshClient sshClient = SshClientFactory.createSsh("root", "752513", "192.168.2.55", 22);
        sshClient.copyFile( "/Users/kingapex/work/crm.txt", "/home/crm.txt");
        sshClient.disconnect();
    }

~~~





## 动态html元素

>  /static/demo.html



## 分页列表

view:


> http://localhost:8080/view/demo/list

data:


> http://localhost:8080/data/demo/pages



### 说明：

#### html:

~~~html
<table id="example2" class="table table-bordered table-hover">
    <thead>
    <tr>
        <th>用户名</th>
        <th>ip</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
~~~

#### javascript:

~~~javascript
    $(function () {
        $("#example2").DataTable({
            "pageLength": 2,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/demo/pages',
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    data: "name"
                },
                {
                    data: "ip"
                },
                {
                    data: null,
                    "render": function (data, type, row) {
                        var btnHtml = "编辑";
                        return btnHtml;
                    }
                }
            ]
        });

    });
~~~



#### controller

~~~java
@RequestMapping("/pages")
public WebPage pagesDemo(int pageNo,int pageSize) {
  IPage<Machine> page = machineMapper.selectPage(new Page<>(pageNo, pageSize),null);
  WebPage webPage = PageConvert.convert(page);
  return webPage;
}
~~~

