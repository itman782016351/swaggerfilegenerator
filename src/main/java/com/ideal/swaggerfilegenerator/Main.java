package com.ideal.swaggerfilegenerator;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author zhaopei
 * @create 2019-09-20 10:16
 */
public class Main {
    public static void main(String[] args) throws IOException, TemplateException {
       String dir=System.getProperty("user.dir");
        System.out.println(dir);
       File dirfile=new File(dir);
        File[] directories = dirfile.listFiles();
        Processor processor = new Processor();
        for (File directory : directories) {
            if (directory.isDirectory()) {
                File[] execelfiles = directory.listFiles();
                for (File execelfile : execelfiles) {
                    processor.process(execelfile);
                }
            }

        }
    }

}
