package ru.netology.controller;
import org.springframework.web.bind.annotation.*;
import ru.netology.model.Post;
import ru.netology.service.PostService;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public List<Post> all(HttpServletResponse resp) {
        return service.all();
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable long id, HttpServletResponse resp) {
        return service.getById(id);
    }

    @PostMapping
    public Post save(@RequestBody Post post) {
        return service.save(post);
    }

    @DeleteMapping("/{id}")
    public void removeById(long id, HttpServletResponse resp) {
        service.removeById(id);
    }

    public void save(BufferedReader reader, HttpServletResponse resp) {
    }
}