package com.microyu.server.template;

import com.microyu.server.http.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateResolver {
    public static final Pattern regex = Pattern.compile("\\$\\{(.*?)}");

    public static String resolve(String content, Request request) {
        Matcher matcher = regex.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = request.getServletContext().getAttribute(key);

            if (value == null) {
                matcher.appendReplacement(sb, "");
            } else {
                //把group(1)得到的数据，替换为value
                matcher.appendReplacement(sb, value.toString());
            }
        }
        // 将源文件后续部分添加至尾部
        matcher.appendTail(sb);
        return sb.toString();
//        String result = sb.toString();
//        return result.length() == 0 ? content : result;
    }
}
