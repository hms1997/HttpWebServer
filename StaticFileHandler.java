import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StaticFileHandler {
    private static final String PUBLIC_DIR = "public";
    private static final Map<String, String> mimeTypes = new HashMap<>();

    static
    {
        mimeTypes.put("html", "text/html");
        mimeTypes.put("txt", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("ico", "image/x-icon");
        mimeTypes.put("json", "application/json");
    }

    public static String serveFile(String path) throws IOException {
        if(path.equals("/")) path = "/index.txt";
        Path filePath = Paths.get(PUBLIC_DIR + path);
        if(!Files.exists(filePath) || Files.isDirectory(filePath)) return null;

        return Files.readString(filePath);
    }

    public static String getMimeType(String path) {
        int index = path.lastIndexOf('.');
        if(index == -1) return "application/octet-stream";
        String extension = path.substring(index + 1);
        return mimeTypes.getOrDefault(extension, "application/octet-stream");
    }

    public static int getFileLength(String path) throws IOException {
        Path filePath = Paths.get(PUBLIC_DIR + path);
        return (int) Files.size(filePath);
    }
}
