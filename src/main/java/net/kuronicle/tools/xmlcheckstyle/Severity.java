package net.kuronicle.tools.xmlcheckstyle;

public enum Severity {
    IGNORE("ignore"), WARNING("warning"), ERROR("error");
    
    private String value;
    
    private Severity(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static Severity convert(String value) {
        if (IGNORE.getValue().equals(value)) {
            return IGNORE;
        } else if(WARNING.getValue().equals(value)) {
            return WARNING;
        } else if(ERROR.getValue().equals(value)) {
            return ERROR;
        } else {
            throw new RuntimeException("Illegal Severity value. value=" + value);
        }
    }
}
