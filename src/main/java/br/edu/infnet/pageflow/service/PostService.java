package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.model.Post;
import br.edu.infnet.pageflow.repository.PostRepository;
import br.edu.infnet.pageflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Collection<Post> getPosts() {
        return postRepository.getAllPosts(Sort.by(Sort.Direction.ASC, "title"));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}
