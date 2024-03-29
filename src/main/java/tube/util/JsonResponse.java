package tube.util;

import com.google.appengine.repackaged.com.google.api.client.json.Json;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.google.gson.Gson;

// class đơn giản phục vụ cho việc trả về json theo mẫu.
public class JsonResponse {

    private int status;
    private String message;
    private Object data;
    @JsonIgnore
    private static Gson gson = new Gson();

    public JsonResponse() {
    }

    public JsonResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public JsonResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JsonResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public String toJsonString(){
        return gson.toJson(this);
    }
}
