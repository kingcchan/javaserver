import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;

public class Server {
  private static HttpServer server;

  public static void main(String[] args) throws Exception {
    Start();
  }

  public static void Start() throws Exception {
    server = HttpServer.create(new InetSocketAddress(8081), 0);
    server.createContext("/", new MainHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
  }

  public static void Stop() {
    server.stop(0);
  }

  static class MainHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
      String root = "www";
      URI uri = t.getRequestURI();
      String path = uri.getPath();
      File file = new File(root + path).getCanonicalFile();

      if (!file.isFile()) {
        // Object does not exist or is not a file, show index.html
        File ifile = new File ("www/index.html");
        byte [] bytearray  = new byte [(int)ifile.length()];
        FileInputStream fis = new FileInputStream(ifile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(bytearray, 0, bytearray.length);

        // ok, we are ready to send the response.
        t.sendResponseHeaders(200, ifile.length());
        OutputStream os = t.getResponseBody();
        os.write(bytearray, 0, bytearray.length);
        os.close();
      } else {
        // Object exists and is a file: accept with response code 200.
        String mime = "text/html";
        if(path.substring(path.length()-3).equals(".js")) mime = "application/javascript";
        if(path.substring(path.length()-3).equals("css")) mime = "text/css";            

        Headers h = t.getResponseHeaders();
        h.set("Content-Type", mime);
        t.sendResponseHeaders(200, 0);              

        OutputStream os = t.getResponseBody();
        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0) {
          os.write(buffer,0,count);
        }
        fs.close();
        os.close();
      }  
    }
  }
}
