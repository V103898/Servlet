
package ru.netology.servlet;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.netology.config.AppConfig;
import ru.netology.controller.PostController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.regex.Pattern;



public class MainServlet extends HttpServlet {
    private static final String POSTS_PATH = "/api/posts";
    private static final String POSTS_ID_PATH = "/api/posts/\\d+";
    private static final Pattern ID_PATTERN = Pattern.compile("/api/posts/(\\d+)");

    private PostController controller;

    @Override
    public void init() {
        var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (method.equals("GET") && path.equals(POSTS_PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals("GET") && path.matches(POSTS_ID_PATH)) {
                final var id = extractId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals(POSTS_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches(POSTS_ID_PATH)) {
                final var id = extractId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long extractId(String path) {
        var matcher = ID_PATTERN.matcher(path);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Invalid URL: " + path);
        }
    }
}
