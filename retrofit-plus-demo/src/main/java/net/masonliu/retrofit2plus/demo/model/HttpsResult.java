package net.masonliu.retrofit2plus.demo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liumeng on 3/29/16.
 */
public class HttpsResult {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
