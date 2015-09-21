package net.kuronicle.tools.xmlcheckstyle;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        XmlCheckStyle xmlCheckStyle = applicationContext.getBean("xmlCheckStyle", XmlCheckStyle.class);
        Map<String, List<CheckError>> errorMap = xmlCheckStyle.check();
        xmlCheckStyle.outputResult(errorMap);
        
        if(applicationContext != null) {
            ((AbstractApplicationContext) applicationContext).close();
        }
            
    }

}
