package ru.netology.repository;
import ru.netology.model.Post;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1); // Start from 1

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            // New post
            long newId = counter.getAndIncrement();
            post.setId(newId);
            posts.put(newId, post);
            return post;
        } else {

            return posts.compute(post.getId(), (id, existing) -> {
                if (existing == null) {
                    return post;
                }

                existing.setContent(post.getContent());
                return existing;
            });
        }
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}