package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.CommentResponse;
import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.dto.PostResponse;
import br.edu.infnet.pageflow.dto.UserInfoResponse;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.entities.ids.PostLikeRelationId;
import br.edu.infnet.pageflow.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostTagRelationRepository postTagRelationRepository;
    private final CommentRepository commentRepository;
    private final PostCommentRelationRepository postCommentRelationRepository;
    private final PostLikeRelationRepository postLikeRelationRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, PostTagRelationRepository postTagRelationRepository, CommentRepository commentRepository, PostCommentRelationRepository postCommentRelationRepository, PostLikeRelationRepository postLikeRelationRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.postTagRelationRepository = postTagRelationRepository;
        this.commentRepository = commentRepository;
        this.postCommentRelationRepository = postCommentRelationRepository;
        this.postLikeRelationRepository = postLikeRelationRepository;
    }

    public Post createPost(PostRequest postRequest) {

        BlogUser author = userRepository.findAuthorById(postRequest.getAuthorId());

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor((Author) author);

        Optional<Category> category = categoryRepository.findById(postRequest.getCategoryId());
        category.ifPresent(post::setCategory);

        return postRepository.save(post);
    }

    public Post findById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Comment> comments = getCommentsByPostId(postId);
        post.setComments(comments);

        return post;
    }

    public List<Comment> getCommentsByPostId(Integer postId) {
        List<PostCommentRelation> relations = postCommentRelationRepository.findAllByPostId(postId);
        return relations.stream().map(PostCommentRelation::getComment).toList();
    }

    public Collection<Post> getPosts() {

//        Collection<Post> posts = (Collection<Post>) postRepository.findAll();
        Collection<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        for (Post post : posts) {
            List<Comment> comments = getCommentsByPostId(post.getId());
            post.setComments(comments);
        }

        return posts;
    }

    public PostResponse getPostWithComments(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        List<PostCommentRelation> postCommentRelations = postCommentRelationRepository.findAllByPostId(postId);

        List<CommentResponse> commentResponses = postCommentRelations.stream()
                .map(pcr -> {
                    Comment comment = pcr.getComment();
                    return new CommentResponse(comment.getId(), comment.getContent(), comment.isApproved());
                })
                .collect(Collectors.toList());

        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setComments(commentResponses);

        return response;
    }

    public Post updatePost(Integer id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.deleteById(id);
    }

    public PostResponse addCommentToPost(Integer postId, Integer commentId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        PostCommentRelation postCommentRelation = new PostCommentRelation();
        postCommentRelation.setPost(post);
        postCommentRelation.setComment(comment);

        postCommentRelationRepository.save(postCommentRelation);

        List<PostCommentRelation> postCommentRelations = postCommentRelationRepository.findAllByPostId(postId);

        List<CommentResponse> commentResponses = postCommentRelations.stream()
                .map(pcr -> {
                    Comment existingComment = pcr.getComment();
                    return new CommentResponse(existingComment.getId(), existingComment.getContent(), existingComment.isApproved());
                })
                .toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setComments(commentResponses);
        postResponse.setAuthor(post.getAuthor());
        postResponse.setCategory(post.getCategory());

        return postResponse;

    }

    public void deleteCommentFromPost(Integer postId, Integer commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        PostCommentRelation postCommentRelation = postCommentRelationRepository.findByPostIdAndCommentId(post.getId(), comment.getId())
                .orElseThrow(() -> new RuntimeException("CommentRelation not found"));

        postCommentRelationRepository.delete(postCommentRelation);
    }

    public Post addTagToPost(Integer postId, Integer tagId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));

        PostTagRelation postTagRelation = new PostTagRelation();
        postTagRelation.setPost(post);
        postTagRelation.setTag(tag);


        return postTagRelationRepository.save(postTagRelation).getPost();
    }

    public void removeTagFromPost(Integer postId, Integer tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        PostTagRelation postTagRelation = postTagRelationRepository.findByPostAndTag(post, tag)
                .orElseThrow(() -> new RuntimeException("Relation not found"));

        postTagRelationRepository.delete(postTagRelation);
    }

    public Collection<Post> findByCategoryName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);

        if (category == null) {
            return null;
        }

        Collection<Post> searchedPosts = postRepository.findByCategoryId(category.getId());
        return searchedPosts;

    }

    public Collection<Post> findByTagName(String tagName) {
        Tag tag = tagRepository.findByName(tagName);
        if (tag == null) {
            return null;
        }
        Collection<Post> searchedPosts = postTagRelationRepository.findByTagId(tag.getId());
        return searchedPosts;
    }

    public Comment getCommentById(Integer commentId) {
        return postCommentRelationRepository.findCommentById(commentId);
    }

    public void savePost(Post existingPost) {
        postRepository.save(existingPost);
    }

    public boolean addLikePost(Integer postId, BlogUser user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        PostLikeRelationId likeId = new PostLikeRelationId(post.getId(), user.getId());

        if(postLikeRelationRepository.existsById(likeId)) {
            return false;
        }

        PostLikeRelation like = new PostLikeRelation(post, user);

        postLikeRelationRepository.save(like);
        return true;
    }

    public void removeLikePost(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        PostLikeRelationId like = new PostLikeRelationId(postId, userId);
        postLikeRelationRepository.deleteById(like);
    }

    public List<UserInfoResponse> getUsersWhoLikedPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<Integer> userIds = postLikeRelationRepository.findUserIdsByPostId(postId);

        if (userIds.isEmpty()) {
            return List.of();
        }

        List<BlogUser> users = (List<BlogUser>) userRepository.findAllById(userIds);

        List<UserInfoResponse> usersInfo = users.stream()
                .map(u -> {
                    UserInfoResponse info = new UserInfoResponse();
                    info.setId(u.getId());
                    info.setName(u.getName());
                    info.setUsername(u.getUsername());
                    info.setEmail(u.getEmail());
                    return info;
                })
                .collect(Collectors.toList());

        return usersInfo;
    }

    public Integer countUsersWhoLikedPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Integer count = postLikeRelationRepository.countByCommentId(postId);

        return count;
    }
}
