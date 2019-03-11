package com.example.mvc.dubbo.framework;

import java.util.Objects;

/**
 * @Author: Lee
 * @Date: 2019/02/01 18:02
 */
public class DuURL {

    private String host;
    private Integer port;

    public DuURL(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }
        DuURL url = (DuURL) obj;
        return Objects.equals(host, url.host) && Objects.equals(port, url.port);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
