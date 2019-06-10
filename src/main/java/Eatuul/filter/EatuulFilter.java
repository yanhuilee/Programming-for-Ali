package Eatuul.filter;

/**
 * @Author: Lee
 * @Date: 2019/05/06 03:12
 */
public abstract class EatuulFilter {
    public abstract String filterType();

    public abstract int filterOrder();

    public abstract void run();
}
