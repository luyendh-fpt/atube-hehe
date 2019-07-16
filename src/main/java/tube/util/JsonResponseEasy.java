package tube.util;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.google.gson.Gson;

// class đơn giản phục vụ cho việc trả về json theo mẫu.
public class JsonResponseEasy {

    private int status;
    private String message;
    private Object data;


    public JsonResponseEasy() {
    }

    public JsonResponseEasy(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
