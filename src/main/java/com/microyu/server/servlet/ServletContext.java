package com.microyu.server.servlet;

import com.microyu.server.http.Cookie;
import com.microyu.server.http.Session;
import com.microyu.server.http.response.HttpResponse;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServletContext {
    private static ServletContext servletContext = new ServletContext();

    public static ServletContext getServletContext() {
        return servletContext;
    }

    private ServletContext() {
        servletMap = new HashMap<>();
        urlMap = new HashMap<>();
        attributeMap = new ConcurrentHashMap<>();
        servletMap = new ConcurrentHashMap<>();
        init();
    }

    private Map<String, HttpServlet> servletMap;
    private Map<String, String> urlMap;
    private Map<String, Object> attributeMap;
    private Map<String, Session> sessionMap;

    public HttpServlet dispatch(String url) {
        return urlMap.get(url) == null ? null : servletMap.get(urlMap.get(url));
    }

    //从web.xml读到servlet映射
    public void init() {
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(this.getClass().getResource("/webapp/WEB-INF/web.xml").getFile());
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }

        Element root = doc.getRootElement();
        List<Element> servlets = root.elements("servlet");
        for (Element servlet : servlets) {
            String key = servlet.element("servlet-name").getText();
            String value = servlet.element("servlet-class").getText();
            HttpServlet httpServlet = null;
            try {
                httpServlet = (HttpServlet) Class.forName(value).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            servletMap.put(key, httpServlet);
        }

        List<Element> mappings = root.elements("servlet-mapping");
        for (Element mapping : mappings) {
            String key = mapping.element("url-pattern").getText();
            String value = mapping.element("servlet-name").getText();
            urlMap.put(key, value);
        }
    }

    public Session getSession(String JSESSIONID) {
        return sessionMap.get(JSESSIONID);
    }

    public Session createSession(HttpResponse response){
        Session session = new Session();
        sessionMap.put(session.getId(),session);
        response.addCookie(new Cookie("JSESSIONID", session.getId()));
        return session;
    }

    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }
}
