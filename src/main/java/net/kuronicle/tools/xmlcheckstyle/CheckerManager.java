package net.kuronicle.tools.xmlcheckstyle;

import java.util.List;

import net.kuronicle.tools.xmlcheckstyle.checker.Checker;

public interface CheckerManager {

    public List<Checker> getCheckerList();
}
