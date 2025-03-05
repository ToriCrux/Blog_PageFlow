package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.model.Post;
import br.edu.infnet.pageflow.model.Tag;
import br.edu.infnet.pageflow.repository.PostRepository;
import br.edu.infnet.pageflow.repository.TagRepository;
import br.edu.infnet.pageflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    public Collection<Post> getPosts() {
        return postRepository.getAllPosts(Sort.by(Sort.Direction.ASC, "title"));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post addTagToPost(Integer postId, UUID tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));

        post.getTags().add(tag);
        return postRepository.save(post);
    }

    public Post removeTagFromPost(Integer postId, UUID tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        post.getTags().remove(tag);
        return postRepository.save(post);
    }

    public Collection<Post> findByCategoryOrTagName(String name) {
        return postRepository.findByCategoryOrTagName(name);
    }

}
