package net.kuronicle.tools.xmlcheckstyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.kuronicle.tools.xmlcheckstyle.checker.AttributeValuePatternChecker;
import net.kuronicle.tools.xmlcheckstyle.checker.Checker;
import net.kuronicle.tools.xmlcheckstyle.checker.LoggingChecker;

import org.junit.Test;

public class XmlCheckStyleTest {

    @Test
    public void testCheck() throws Exception {
        XmlCheckStyle target = new XmlCheckStyle();
        List<Checker> checkerList = new ArrayList<Checker>();
        checkerList.add(new LoggingChecker());
        checkerList.add(new AttributeValuePatternChecker("file/" + "error",
                "line", "^[1].*", "warning"));
        target.setCheckerList(checkerList);
        Map<String, List<CheckError>> errorMap = target.check();

        for (Entry<String, List<CheckError>> entry : errorMap.entrySet()) {
            String fileName = entry.getKey();
            List<CheckError> errorList = entry.getValue();

            System.out.println("fileName=" + fileName);

            for (CheckError error : errorList) {
                System.out.println(error);
            }
        }
    }

    @Test
    public void testOutputResult() throws Exception {

        Map<String, List<CheckError>> mapError = new HashMap<String, List<CheckError>>();

        String fileName = "./hoge/test.xml";
        CheckError error = new CheckError();
        error.setLine(123);
        error.setMessage("message");
        error.setSeverity(Severity.WARNING);
        error.setSource("hoge.class");
        List<CheckError> errors = new ArrayList<CheckError>();
        errors.add(error);
        mapError.put(fileName, errors);

        XmlCheckStyle target = new XmlCheckStyle();
        target.outputResult(mapError);
    }
    
    @Test
    public void testCheckAndOutputResult() throws Exception {
        XmlCheckStyle target = new XmlCheckStyle();
        List<Checker> checkerList = new ArrayList<Checker>();
        checkerList.add(new LoggingChecker());
        checkerList.add(new AttributeValuePatternChecker("file/" + "error",
                "line", "^[1].*", "warning"));
        target.setCheckerList(checkerList);
        Map<String, List<CheckError>> errorMap = target.check();
        target.outputResult(errorMap);
        
    }

}
