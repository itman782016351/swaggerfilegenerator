import com.ideal.swaggerfilegenerator.FreemarkerUtil;
import com.ideal.swaggerfilegenerator.Processor;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaopei
 * @create 2019-09-20 12:02
 */
public class Tests {
    FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
    Processor processor = new Processor();

    @Test
    public void testTemplateRender() throws IOException, TemplateException {
        Map map = new HashMap<>();
        map.put("test", "test content....");
        String str = freemarkerUtil.render("swagger.ftl", map);
        System.out.println(str);
    }

    @Test
    public void testFileApi() throws FileNotFoundException {
        File f = new File("C:\\Users\\zhaopei\\Desktop\\QueryResourceAccessAbility_1.0.xlsx");
        String result = "result";
        String execelPath = f.getPath();
        File target = new File(execelPath.substring(0, execelPath.lastIndexOf(".xlsx")) + ".yaml");
        if (target.exists()) {
            target.delete();
        }
        PrintWriter pw = new PrintWriter(target);
        pw.print(result);
        pw.flush();
        pw.close();
    }

    @Test
    public void testPoi() throws IOException {
        File f = new File("C:\\Users\\zhaopei\\Desktop\\QueryResourceAccessAbility_1.0.xlsx");
        processor.getRenderMap(f);
    }
}
