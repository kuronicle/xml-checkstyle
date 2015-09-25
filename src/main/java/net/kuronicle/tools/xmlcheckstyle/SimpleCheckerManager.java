package net.kuronicle.tools.xmlcheckstyle;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import net.kuronicle.tools.xmlcheckstyle.checker.Checker;

public class SimpleCheckerManager implements CheckerManager {

    @Setter
    protected List<Checker> checkerList = new ArrayList<Checker>();
    
    public List<Checker> getCheckerList() {
        return checkerList;
    }

}
