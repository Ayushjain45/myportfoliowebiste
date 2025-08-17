import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class PortfolioServer {

    private static final String YOUR_NAME = "Ayush Jain";   
    private static final String YOUR_ROLE = "Java Developer | Portfolio Website";
    private static final String PHOTO_FILE = "me.jpg";      
    private static final String PHOTO_ROUTE = "/me.jpg";    
    private static final String PHOTO_MIME = "image/jpeg";  

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", new HomeHandler());

        server.createContext(PHOTO_ROUTE, exchange -> {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendText(exchange, 405, "Method Not Allowed");
                return;
            }
            Path img = Path.of(PHOTO_FILE).toAbsolutePath();
            if (!Files.exists(img)) {
                sendText(exchange, 404, "Photo not found. Place " + PHOTO_FILE + " in this folder.");
                return;
            }
            byte[] data = Files.readAllBytes(img);
            exchange.getResponseHeaders().add("Content-Type", PHOTO_MIME);
            exchange.sendResponseHeaders(200, data.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("🚀 Portfolio running at http://localhost:" + PORT);
    }

    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendText(exchange, 405, "Method Not Allowed");
                return;
            }

            String html =
                    "<!DOCTYPE html>" +
                    "<html lang='en'>" +
                    "<head>" +
                    "  <meta charset='UTF-8'>" +
                    "  <title>" + YOUR_NAME + " - Portfolio</title>" +
                    "  <style>" +
                    "    body { font-family: Arial, sans-serif; " +
                    "           background: linear-gradient(-45deg, #ff9a9e, #fad0c4, #a18cd1, #fbc2eb); " +
                    "           background-size: 400% 400%; animation: gradientBG 15s ease infinite; " +
                    "           margin: 0; padding: 0; text-align: center; }" +
                    "    @keyframes gradientBG { " +
                    "      0% { background-position: 0% 50%; }" +
                    "      50% { background-position: 100% 50%; }" +
                    "      100% { background-position: 0% 50%; }" +
                    "    }" +
                    "    header { padding: 50px 20px; }" +
                    "    .card { background: white; padding: 30px; border-radius: 14px; " +
                    "            box-shadow: 0 6px 15px rgba(0,0,0,0.2); display: inline-block; max-width: 700px; margin-bottom: 40px; }" +
                    "    img { width: 200px; height: 200px; border-radius: 50%; object-fit: cover; margin-bottom: 20px; }" +
                    "    h1 { margin: 10px 0; font-size: 28px; }" +
                    "    h2 { margin-top: 40px; font-size: 24px; color: #333; }" +
                    "    p { color: #444; font-size: 18px; line-height: 1.5; }" +
                    "    .links a { display: inline-block; margin: 10px; padding: 10px 16px; " +
                    "               border-radius: 8px; text-decoration: none; font-weight: bold; " +
                    "               background: #0077ff; color: white; transition: 0.3s; }" +
                    "    .links a:hover { background: #0056b3; }" +
                    "    section { padding: 40px 20px; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <header>" +
                    "    <div class='card'>" +
                    "      <img src='" + PHOTO_ROUTE + "' alt='My Photo'>" +
                    "      <h1>" + YOUR_NAME + "</h1>" +
                    "      <p>" + YOUR_ROLE + "</p>" +
                    "      <div class='links'>" +
                    "        <a href='mailto:ayushjain00111@gmail.com'>📧 Gmail</a>" +
                    "        <a href='https://github.com/Ayushjain45' target='_blank'>💻 GitHub</a>" +
                    "        <a href='https://www.linkedin.com/in/ayush-jain-4854522a9' target='_blank'>🔗 LinkedIn</a>" +
                    "      </div>" +
                    "    </div>" +
                    "  </header>" +

                    "  <section>" +
                    "    <h2>👨‍💻 About Me</h2>" +
                    "    <p>Hello! I am " + YOUR_NAME + ", a passionate Java Developer who loves building scalable applications and solving real-world problems with clean code.</p>" +
                    "  </section>" +

                    "  <section>" +
                    "    <h2>🎓 Education</h2>" +
                    "    <p>Bachelor of Technology in Electronics and telecommunication engineering<br>chandigrah group of colleges , 2023</p>" +
                    "  </section>" +

                    "  <section>" +
                    "    <h2>🤝 Let's Work Together</h2>" +
                    "    <p>I am open to collaborating on exciting Java projects, internships, and full-time roles. Let's connect and create something amazing together!</p>" +
                    "  </section>" +

                    "  <section>" +
                    "    <h2>📬 Contact Me- 8054220702</h2>" +
                    "    <p>You can reach out to me via email or connect with me on LinkedIn & GitHub.</p>" +
                    "    <div class='links'>" +
                    "      <a href='mailto:ayushjain00111@gmail.com'>✉️ Send Email</a>" +
                    "      <a href='https://www.linkedin.com/in/ayush-jain-4854522a9' target='_blank'>🔗 LinkedIn</a>" +
                    "      <a href='https://github.com/Ayushjain45' target='_blank'>💻 GitHub</a>" +
                    "    </div>" +
                    "  </section>" +

                    "</body>" +
                    "</html>";

            byte[] bytes = html.getBytes();
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static void sendText(HttpExchange exchange, int status, String message) throws IOException {
        byte[] bytes = message.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
