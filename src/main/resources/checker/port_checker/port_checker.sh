check(){
   r=`netstat -ntlp | awk '{print $4}' | grep :${port}`
    if [ "$r" == "" ]
    then
        return 0
    else
        return 1
    fi
}
