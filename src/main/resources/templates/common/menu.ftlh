<li class="nav-item has-treeview menu-open">
    <a href="#" class="nav-link ">
        <i class="nav-icon iconfont icon-jichu"></i>
        <p>
            基础信息 <i class="right fas fa-angle-left"></i>
        </p>
    </a>

    <ul class="nav nav-treeview">
        <li class="nav-item ">
            <a href="${context_path}/view/machines/list" id="m-machine" class="nav-link">
                <i class="nav-icon iconfont icon-fuwuqi"></i>
                <p>
                    机器
                </p>
            </a>
        </li>

        <li class="nav-item ">
            <a href="/view/repository/list" id="m-repository" class="nav-link">
                <i class="nav-icon iconfont icon-git"></i>
                <p>
                    仓库
                </p>
            </a>
        </li>

        <li class="nav-item ">
            <a href="${context_path}/view/tag/list" id="m-tags" class="nav-link">
                <i class="nav-icon iconfont icon-tag"></i>
                <p>
                    标签
                </p>
            </a>
        </li>

        <li class="nav-item ">
            <a href="${context_path}/view/machinesGroup/list" id="m-machine-group" class="nav-link">
                <i class="nav-icon fas fa-copy"></i>
                <p>
                    分组
                </p>
            </a>
        </li>

    </ul>

</li>


<li class="nav-item  has-treeview menu-open">
    <a href="#" class="nav-link">
        <i class="nav-icon  iconfont icon-deploy1"></i>
        <p>
            编排部署
            <i class="fas fa-angle-left right"></i>

        </p>
    </a>

    <ul class="nav nav-treeview">
        <li class="nav-item ">
            <a href="${context_path}/view/deployment/list" class="nav-link" id="m-deploy">
                <i class="nav-icon iconfont icon-bianpaixin"></i>
                <p>
                    部署
                </p>
            </a>
        </li>
        <li class="nav-item ">
            <a href="/view/deploymentSet/list" class="nav-link" id="m-deploy-set">
                <i class="nav-icon iconfont icon-deploy"></i>
                <p>
                    部署集
                </p>
            </a>
        </li>

        <li class="nav-item ">
            <a href="${context_path}/view/deploymentGroup/list" id="m-deploy-group" class="nav-link">
                <i class="nav-icon fas fa-copy"></i>
                <p>
                    分组
                </p>
            </a>
        </li>
    </ul>
</li>


<li class="nav-item  has-treeview menu-open">
    <a href="#" class="nav-link">
        <i class="nav-icon  iconfont icon-kindsofplugins"></i>
        <p>
            插件
            <i class="fas fa-angle-left right"></i>

        </p>
    </a>

    <ul class="nav nav-treeview">
        <li class="nav-item ">
            <a href="/view/executor/list" class="nav-link" id="m-executor">
                <i class="nav-icon iconfont icon-cpu"></i>
                <p>
                    执行器
                </p>
            </a>
        </li>
        <li class="nav-item ">
            <a href="/view/checker/list" class="nav-link" id="m-checker">
                <i class="nav-icon iconfont icon-280"></i>
                <p>
                    检查器
                </p>
            </a>
        </li>
    </ul>
</li>


<li class="nav-item  has-treeview menu-open">
    <a href="#" class="nav-link">
        <i class="nav-icon  iconfont icon-peizhi"></i>
        <p>
            配置
            <i class="fas fa-angle-left right"></i>

        </p>
    </a>

    <ul class="nav nav-treeview">
        <li class="nav-item ">
            <a href="${context_path}/view/env-project/list" class="nav-link"  id="env-vars">
                <i class="nav-icon iconfont icon-chengxu"></i>
                <p>
                    环境变量
                </p>
            </a>
        </li>
        <li class="nav-item ">
            <a href="/view/config_project" class="nav-link" id="m-config-file">
                <i class="nav-icon iconfont icon-peizhiwenjian" id="m-config-file"></i>
                <p>
                    配置文件
                </p>
            </a>
        </li>
        <li class="nav-item ">
            <a href="/view/keys/list" class="nav-link" id="m-keys">
                <i class="nav-icon iconfont icon-peizhiwenjian" id="m-keys"></i>
                <p>
                    密钥管理
                </p>
            </a>
        </li>
        <li class="nav-item ">
            <a href="${context_path}/view/user/list" class="nav-link" id="m-user">
                <i class="nav-icon iconfont icon-peizhiwenjian" id="m-user"></i>
                <p>
                    用户管理
                </p>
            </a>
        </li>
    </ul>

</li>
