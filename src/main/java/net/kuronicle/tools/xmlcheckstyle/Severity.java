package net.kuronicle.tools.xmlcheckstyle;

public enum Severity {
    WARNING("warning"), ERROR("error");
    
    private String value;
    
    private Severity(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static Severity convert(String value) {
        if(Severity.WARNING.getValue().equals(value)) {
            return WARNING;
        } else if(Severity.ERROR.getValue().equals(value)) {
            return ERROR;
        } else {
            throw new RuntimeException("Illegal Severity value. value=" + value);
        }
    }
}
