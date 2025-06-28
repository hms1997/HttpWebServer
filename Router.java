import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {

    // Route map: method + path → handler
    private static final Map<String, Function<HTTPRequest, String>> routes = new HashMap<>();

    public static void addRoute(String method, String path, Function<HTTPRequest, String> handler) {
        routes.put(method + " " + path, handler);
    }

    public static String handleRequest(HTTPRequest request) {
        String key = request.method + " " + request.path;
        Function<HTTPRequest, String> handler = routes.get(key);
        if (handler == null) return null;
        return handler.apply(request);
    }
}
