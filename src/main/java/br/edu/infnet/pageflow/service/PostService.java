package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Collection<Post> getPosts() {
        return postRepository.getAllPosts();
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}
