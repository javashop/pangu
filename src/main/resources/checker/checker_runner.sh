#!/bin/bash
#检测总时长，以秒为单位
duration=${duration}


#执行检测的function，由具体的检查器提供，由这变量赋值体现
${checkFun}

int=1
result=0
while(( $int<=$duration ))
do
   check
   if [[ $? == 1 ]]; then
   	 result=1
   	 break
   else
	   result=0
   	 sleep 1s
   fi

   let "int++"
done

echo $result
exit
