git --version
if [[ $? == 0 ]]; then
    echo 'git已存在'
else
    yum -y install git
fi

<#if repository.authType == 'publickey'>

#对gitee和github开放host信任，如有其它域名信任自行添加
grep 'gitee.com' ~/.ssh/known_hosts

if [[ $? -ne  0 ]]; then
 ssh-keyscan -t rsa gitee.com >> ~/.ssh/known_hosts
fi

grep 'github.com' ~/.ssh/known_hosts

if [[ $? -ne  0 ]]; then
 ssh-keyscan -t rsa github.com >> ~/.ssh/known_hosts
fi

</#if>

mkdir -p ${workspace}/source
cd ${workspace}/source

echo '开始clone代码'
<#assign git_url = repository.address />
<#assign username = repository.username />
<#assign password = repository.password />
<#assign branch = repository.branch />

<#if branch == '' >
  <#assign branch = 'master' />
</#if>

<#if !git_url?starts_with("http://")  &&  !git_url?starts_with("https://")  &&  !git_url?starts_with("git@") >
echo 'git仓库地址格式不正确'
<#elseif repository.authType == 'publickey'>
git clone ${git_url}
<#else>
  <#if username== '' || password == '' >
echo '必须提供仓库的用户名和密码参数'
  <#else>
    <#assign url = git_url?replace("://","://"+ username+":"+ password +"@") />
git clone ${url} --branch ${branch}
  </#if>
</#if>


