import java.io.*;
import java.net.*;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("🚀 Server running on http://localhost:" + port);

        // Register API routes
        Router.addRoute("GET", "/api/hello", (req) -> "{\"message\": \"Hello from API!\"}");
        Router.addRoute("POST", "/api/echo", (req) -> "{\"you_sent\": \"" + req.body + "\"}");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            HTTPRequest request = HTTPRequest.parse(in);
            if (request == null) {
                clientSocket.close();
                continue;
            }

            String path = request.path;
            System.out.println("📥 " + request.method + " " + path);

            // Handle static files
            if (!path.startsWith("/api")) {
                try {
                    String content = StaticFileHandler.serveFile(path);
                    if (content == null) {
                        writeResponse(out, "404 Not Found", "text/plain", "File not found");
                    } else {
                        String contentType = StaticFileHandler.getMimeType(path);
                        int length = StaticFileHandler.getFileLength(path);
                        writeResponse(out, "200 OK", contentType, content, length);
                    }
                } catch (IOException e) {
                    writeResponse(out, "500 Internal Server Error", "text/plain", "Server error");
                }
            } else {
                // Handle API route
                String json = Router.handleRequest(request);
                if (json == null) {
                    writeResponse(out, "404 Not Found", "application/json", "{\"error\":\"Not Found\"}");
                } else {
                    writeResponse(out, "200 OK", "application/json", json);
                }
            }

            clientSocket.close();
        }
    }

    private static void writeResponse(BufferedWriter out, String status, String contentType, String body, int contentLength) throws IOException {
        out.write("HTTP/1.1 " + status + "\r\n");
        out.write("Content-Type: " + contentType + "\r\n");
        out.write("Content-Length: " + contentLength + "\r\n");
        out.write("Connection: close\r\n");
        out.write("\r\n");
        out.write(body);
        out.flush();
    }

    private static void writeResponse(BufferedWriter out, String status, String contentType, String body) throws IOException {
        writeResponse(out, status, contentType, body, body.length());
    }
}
