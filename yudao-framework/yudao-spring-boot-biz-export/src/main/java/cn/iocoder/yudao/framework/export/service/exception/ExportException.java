package cn.iocoder.yudao.framework.export.service.exception;


public class ExportException extends RuntimeException {

    public ExportException(String msg) {
        super(msg);
    }

    public ExportException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
