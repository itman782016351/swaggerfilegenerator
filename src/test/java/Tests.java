import com.ideal.swaggerfilegenerator.EntityNode;
import com.ideal.swaggerfilegenerator.FreemarkerUtil;
import com.ideal.swaggerfilegenerator.Processor;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

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

    @Test
    public void testReplaceAll(){
        String demo="我\r\n们\r\n是\r\n程序猿。";
        System.out.println("转换前内容:"+demo);
        System.out.println("转换后内容:"+ StringUtils.replaceAll(demo,"\r\n",""));
    }

    @Test
    public void testConcurrentModify() {
        List<EntityNode> list = new ArrayList<>();
        list.add(new EntityNode("1", "", false, "", true));
        list.add(new EntityNode("2", "", false, "", true));
        list.add(new EntityNode("3", "", false, "", true));
        for (EntityNode entityNode : list) {
            System.out.println(entityNode.isRequired());
            System.out.println(entityNode.getPropertyName());
        }
        Iterator<EntityNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            EntityNode next = iterator.next();
            if (next.getPropertyName().equals("") && next.isRequired()) {
                next.setPropertyName("222");
                next.setRequired(false);
            }
        }
        for (EntityNode entityNode : list) {
            System.out.println(entityNode.isRequired());
            System.out.println(entityNode.getPropertyName());
        }
    }
}
