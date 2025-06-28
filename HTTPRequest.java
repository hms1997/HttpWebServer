import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    public String method;
    public String path;
    public String version;
    public Map<String, String> headers = new HashMap<>();
    public String body = "";

    public static HTTPRequest parse(BufferedReader bufferedReader) throws IOException {
        HTTPRequest request = new HTTPRequest();
        //read request line
        String requestLine = bufferedReader.readLine();
        if(requestLine == null || requestLine.isEmpty()) return null;

        String[] parts = requestLine.split(" ");
        request.method = parts[0];
        request.path = parts[1];
        request.version = parts[2];

        //read headers
        String line;
        while((line  = bufferedReader.readLine()) != null && !line.isEmpty()) {
            int index = line.indexOf(":");
            if(index != -1) {
                String key = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();
                request.headers.put(key, value);
            }
        }
        //read body if content length exists
        String contentLength = request.headers.get("Content-Length");
        if(contentLength != null) {
            int length = Integer.parseInt(contentLength);
            char[] bodyChars = new char[length];
            bufferedReader.read(bodyChars);
            request.body = new String(bodyChars);
        }
        return request;
    }
}
