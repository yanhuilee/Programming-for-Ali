package Eatuul.filter.pre;

import Eatuul.filter.EatuulFilter;

/**
 * PreFilter,前置执行过滤器，负责封装请求。步骤如下所示
 * (1)封装请求头
 * (2)封装请求体
 * (3)构造出RestTemplate能识别的RequestEntity
 * (4)将RequestEntity放入全局threadlocal之中
 * @Author: Lee
 * @Date: 2019/05/06 03:12
 */
public class RequestWrapperFilter extends EatuulFilter {
    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public void run() {

    }
}
