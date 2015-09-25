package net.kuronicle.tools.xmlcheckstyle.checker;

import java.util.HashSet;
import java.util.Set;

import lombok.Setter;

public abstract class BaseChecker implements Checker {
    
    @Setter
    protected Set<String> ignoreFileSet = new HashSet<String>();
    
    public void addIgnoreFile(String fileName) {
        ignoreFileSet.add(fileName);
    }

    public boolean isIgnore(String fileName) {
        return ignoreFileSet.contains(fileName);
    }
}
