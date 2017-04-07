package co.paystack.android.ui;

public class AuthSingleton {
    private static AuthSingleton instance = new AuthSingleton();
    private String responseJson = "{\"status\":\"requery\",\"message\":\"Reaffirm Transaction Status on Server\"}";
    private String url = "";

    private AuthSingleton() {
    }

    public static AuthSingleton getInstance() {
        return instance;
    }

    String getUrl() {
        return url;
    }

    public AuthSingleton setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getResponseJson() {
        return responseJson;
    }

    AuthSingleton setResponseJson(String responseJson) {
        this.responseJson = responseJson;
        return this;
    }
}