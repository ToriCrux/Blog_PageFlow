package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.dto.UserInfoResponse;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.entities.ids.CommentLikeRelationId;
import br.edu.infnet.pageflow.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostCommentRelationRepository postCommentRelationRepository;
    private final PostRepository postRepository;
    private final CommentLikeRelationRepository commentLikeRelationRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostCommentRelationRepository postCommentRelationRepository, PostRepository postRepository, CommentLikeRelationRepository commentLikeRelationRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postCommentRelationRepository = postCommentRelationRepository;
        this.postRepository = postRepository;
        this.commentLikeRelationRepository = commentLikeRelationRepository;
        this.userRepository = userRepository;
    }

    public Collection<Comment> getComments(){
        return (Collection<Comment>) commentRepository.findAll();
    }

    public Collection<Comment> getCommentsByPost(Integer postId) {

        Optional<Post> existingPost = postRepository.getPostById(postId);

        if(!existingPost.isPresent()){
            throw new RuntimeException("Post não encontrado");
        }

        List<PostCommentRelation> relations = postCommentRelationRepository.findAllByPostId(postId);
        return relations.stream().map(PostCommentRelation::getComment).toList();
    }

    public Comment createComment(CommentRequest commentRequest){

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setApproved(commentRequest.isApproved());

        return commentRepository.save(comment);
    }

    public Comment updateComment(Integer id, CommentRequest commentRequest){

        Optional<Comment> existingComment = commentRepository.findById(id);

        if(existingComment.isPresent()){
            Comment updatedComment = existingComment.get();
            updatedComment.setContent(commentRequest.getContent());
            updatedComment.setApproved(commentRequest.isApproved());
            updatedComment.setUpdatedAt(LocalDateTime.now());

            return commentRepository.save(updatedComment);
        }

        return null;
    }

    public void deleteComment(Integer id){

        if(!commentRepository.existsById(id)){
            throw new RuntimeException("Commentário não encontrado");
        }

        commentRepository.deleteById(id);
    }


    public boolean addLikeComment(Integer commentId, BlogUser user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        CommentLikeRelationId likeId = new CommentLikeRelationId(comment.getId(), user.getId());

        if(commentLikeRelationRepository.existsById(likeId)) {
            return false;
        }

        CommentLikeRelation like = new CommentLikeRelation(comment, user);

        commentLikeRelationRepository.save(like);

        return true;
    }

    public void removeLikeComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        CommentLikeRelationId like = new CommentLikeRelationId(commentId, userId);

        commentLikeRelationRepository.deleteById(like);
    }

    public List<UserInfoResponse> getUsersWhoLikedComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        List<Integer> userIds = commentLikeRelationRepository.findUserIdsByCommentId(commentId);

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

    public Integer countUsersWhoLikedComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Integer count = commentLikeRelationRepository.countByCommentId(commentId);

        return count;
    }

}
