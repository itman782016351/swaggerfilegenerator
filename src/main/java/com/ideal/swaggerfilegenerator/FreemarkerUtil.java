package com.ideal.swaggerfilegenerator;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * @author zhaopei
 * @create 2019-09-20 10:28
 */
public class FreemarkerUtil {
    private Template template;
    private Configuration configuration;

    public void initFreemarker() {
        String basePakagePath = "/";
        configuration = new Configuration();
        configuration.setLocale(new Locale("zh_CN"));
        configuration.setEncoding(Locale.getDefault(), "UTF-8");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setClassForTemplateLoading(this.getClass(), basePakagePath);
    }

    public String render(String templateName, Map<?, ?> templateMap) throws IOException, TemplateException {
        initFreemarker();
        String out;
        template = configuration.getTemplate(templateName);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Writer writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));// 输出到字节组
        template.process(templateMap, writer);
        writer.flush();
        out = os.toString("UTF-8");
        os.close();
        writer.close();
        return out;
    }
}
