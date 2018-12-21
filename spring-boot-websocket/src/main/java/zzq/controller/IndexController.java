package zzq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 〈功能简述〉<br>
 * 〈跳转控制层〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-21
 */
@Controller
public class IndexController {

    @GetMapping("/websocket/{username}")
    public String  websocket(@PathVariable String username, HttpServletRequest request){
        request.setAttribute("username",username);
        return "websocket";
    }
}
