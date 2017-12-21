package com.test.demo.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * cookies 工具类
 * Created by yhn on 2017/9/5.
 */
public class CookiesUtil {
        public static void set(HttpServletResponse response,
                               String name,
                               String value,
                               int maxAge){
            Cookie cookie = new Cookie(name,value);
            cookie.setPath("/");
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        }
        public static Cookie get(HttpServletRequest request,
                                 String name){
               // request.getCookies();   //此为数组，我们需要遍历获得我们需要的数据
            Map<String,Cookie> cookieMap = readCookieMap(request);
            if(cookieMap.containsKey(name)){
                    return cookieMap.get(name);
            }else{
                return null;
            }
        }
        //将cookie封装为map
        private static Map<String,Cookie> readCookieMap(HttpServletRequest request){
            Map<String,Cookie> cookieMap = new HashMap<>();
                Cookie[] cookies = request.getCookies();
                if(cookies!=null){
                    for (Cookie cookie :cookies){
                        cookieMap.put(cookie.getName(),cookie);
                    }
                }
                return cookieMap;
        }
}
