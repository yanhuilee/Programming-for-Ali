package Eatuul.http;

import Eatuul.filter.EatuulFilter;
import Eatuul.filter.pre.RequestWrapperFilter;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 具体的执行器
 * @Author: Lee
 * @Date: 2019/05/06 03:15
 */
public class EatRunner {

    /**
     * 静态过滤器
     */
    private Map<String , List<EatuulFilter>> hashFiltersByType = new ConcurrentHashMap() {{
        put("pre", new ArrayList<EatuulFilter>() {{
            add(new RequestWrapperFilter());
        }});
    }};

    public EatRunner() {
        hashFiltersByType.put("pre", List<EatuulFilter> list = () -> add());
        List list = new ArrayList<EatuulFilter>();
        list.add(new RequestWrapperFilter());
        hashFiltersByType.put("pre", list);

    }

    public void init(HttpServletRequest req, HttpServletResponse resp) {
    }

    public void preRoute() {
    }

    public void route() {
    }

    public void postRoute() {
    }
}
