package zzq.zzqsimpleframeworkweb.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import zzq.zzqsimpleframeworkcommon.context.GlobalContext;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import zzq.zzqsimpleframeworkcommon.entity.ProjectConstant;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworkweb.servlet.CustomHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * web 全链路上下文管理
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-16 11:19
 */
@Component
@Order(1)
@WebFilter(filterName = "webGlobalContextFilter", urlPatterns = "/*")
public class WebGlobalContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    // 发送请求会执行这个方法
    // 一个doFilter相当于拦截器的执行前和执行后
    // filterChain.doFilter后面的内容就是执行后的内容，假如不执行filterChain.doFilter方法相当于方法被拦截
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            CustomHttpServletRequestWrapper customHttpServletRequestWrapper = new CustomHttpServletRequestWrapper(request);
            String requestHeader = customHttpServletRequestWrapper.getHeader(ProjectConstant.GLOBAL_CONTEXT_HEADER_KEY);
            if (requestHeader != null) {
                GlobalContext globalContext = JacksonUtil.parseJson(requestHeader, new TypeReference<GlobalContext>() {
                });
                ThreadLocalManager.setGlobalContext(globalContext);
            }else{
                //如果没有，就初始化一次，给后续业务及子线程用
                ThreadLocalManager.globalContextThreadLocal.get();
            }
            filterChain.doFilter(customHttpServletRequestWrapper, servletResponse);
        } finally {
            ThreadLocalManager.clear();
        }
    }

    @Override
    public void destroy() {

    }
}