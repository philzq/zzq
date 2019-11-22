package zzq.simple.spring.framework;

import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import zzq.simple.spring.framework.bean.Data;
import zzq.simple.spring.framework.bean.Handler;
import zzq.simple.spring.framework.bean.Param;
import zzq.simple.spring.framework.bean.View;
import zzq.simple.spring.framework.helper.BeanHelper;
import zzq.simple.spring.framework.helper.ControllerHelper;
import zzq.simple.spring.framework.helper.RequestHelper;
import zzq.simple.spring.framework.util.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 请求转发器
 * 该Servlet将会在Web容器启动时加载
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化相关的helper类
        HelperLoader.init();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestMethod = request.getMethod().toUpperCase();
        String requestPath = request.getPathInfo();
        if(StringUtils.isNotEmpty(requestPath)){
            requestPath = requestPath.substring(1);
        }
        //这里根据Tomcat的配置路径有两种情况, 一种是 "/userList", 另一种是 "/context地址/userList".
        String[] splits = requestPath.split("/");
        if (splits.length >= 2) {
            requestPath = "/" + splits[2];
        }

        //根据请求获取处理器(这里类似于SpringMVC中的映射处理器)
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            //初始化参数
            Param param = RequestHelper.createParam(request);

            //调用与请求对应的方法(这里类似于SpringMVC中的处理器适配器)
            Object result;
            Method actionMethod = handler.getControllerMethod();
            if (param == null || param.isEmpty()) {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
            } else {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            }

            //跳转页面或返回json数据(这里类似于SpringMVC中的视图解析器)
            if (result instanceof View) {
                handleViewResult((View) result, request, response);
            } else if (result instanceof Data) {
                handleDataResult((Data) result, response);
            }
        }
    }

    /**
     * 跳转页面
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtils.isNotEmpty(path)) {
            if (path.startsWith("/")) { //重定向
                response.sendRedirect(request.getContextPath() + path);
            } else { //请求转发
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * 返回JSON数据
     */
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JSONUtil.toJsonStr(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
