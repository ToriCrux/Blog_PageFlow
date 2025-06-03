package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.entities.PostCommentRelation;
import br.edu.infnet.pageflow.repository.CommentRepository;
import br.edu.infnet.pageflow.repository.PostCommentRelationRepository;
import br.edu.infnet.pageflow.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostCommentRelationRepository postCommentRelationRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostCommentRelationRepository postCommentRelationRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postCommentRelationRepository = postCommentRelationRepository;
        this.postRepository = postRepository;
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


}
