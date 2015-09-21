package net.kuronicle.tools.xmlcheckstyle;

import lombok.Data;

@Data
public class CheckError {
    private Integer line;
    private Integer column;
    private Severity severity;
    private String message;
    private String source;
}
