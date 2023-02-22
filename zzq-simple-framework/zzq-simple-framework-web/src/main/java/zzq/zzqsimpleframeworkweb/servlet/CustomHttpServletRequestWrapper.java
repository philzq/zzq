/**
 * Kdniao.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package zzq.zzqsimpleframeworkweb.servlet;

import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import zzq.zzqsimpleframeworkjson.JacksonUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version $Id: HttpServletRequestWrapper.java, v 0.1 2016年1月13日 上午10:42:30 Administrator Exp $
 */
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {


    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    // ------------------------------------ 扩展方法 -------------------------------------------

    public String getContextName() {
        String contextName = this.getContextPath();
        if (StringUtils.isNotEmpty(contextName) && contextName.startsWith("/")) {
            contextName = contextName.substring(1);
        }
        return contextName;
    }

    public String getDomainName() {
        return this.getHeader("Host");
    }

    public UserAgent getUserAgent() {
        return UserAgent.parseUserAgentString(this.getHeader("user-agent"));
    }

    public String getReferer() {
        return this.getHeader("Referer");
    }

    public String getSessionId(boolean create) {
        return this.getSession(create) != null ? this.getSession(create).getId() : null;
    }

    public String getCookieId() {
        Cookie cookie = this.getCookie("cookieId");
        return cookie == null ? null : cookie.getValue();
    }

    public Cookie getCookie(String name) {
        Cookie[] cookies = this.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public String getScreenSize() {
        String width = this.getParameter("width");
        String height = this.getParameter("height");
        if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
            return width + "x" + height;
        } else {
            return null;
        }
    }

    public String getClientIp() {
        String ipAddress = this.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = this.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = this.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = this.getRemoteAddr();
        }
        if (StringUtils.isEmpty(ipAddress) || ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
            ipAddress = SystemUtils.getHostName();
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15  
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public Map<String, String> getInputParamMap() {
        Map<String, String> paramMap = new HashMap<>();
        Enumeration<?> paramNames = this.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement().toString();
            paramMap.put(paramName, this.getParameter(paramName));
        }
        return paramMap;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return JacksonUtil.toJSon(this.getInputParamMap());
    }
}