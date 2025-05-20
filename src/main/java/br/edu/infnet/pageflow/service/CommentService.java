package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.entities.BlogUser;
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
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostCommentRelationRepository postCommentRelationRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postCommentRelationRepository = postCommentRelationRepository;
        this.postRepository = postRepository;
        this.userService = userService;
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

    public Collection<Comment> getCommentsByParentComment(Integer parentCommentId) {

        Optional<Comment> existingComment = commentRepository.findById(parentCommentId);

        if(!existingComment.isPresent()){
            throw new RuntimeException("Parent comment not found");
        }

        return (Collection<Comment>) commentRepository.getAllByParentComment(parentCommentId);

    }

    public Comment createComment(CommentRequest commentRequest){

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setApproved(commentRequest.isApproved());
        if(commentRequest.getParentCommentId() != null){
            Optional<Comment> existingParentComment = commentRepository.findById(commentRequest.getParentCommentId());
            if(!existingParentComment.isPresent()){
                throw new RuntimeException("Parent comment not found");
            }
            comment.setParentComment(existingParentComment.get());
        }

        if(commentRequest.getAuthorId() != null){
            Optional<BlogUser> existingAuthor = userService.getUserById(commentRequest.getAuthorId());
            if(!existingAuthor.isPresent()){
                throw new RuntimeException("Author not found");
            }
            comment.setAuthor(existingAuthor.get());
        }

        return commentRepository.save(comment);
    }

    public Comment updateComment(Integer id, CommentRequest commentRequest){

        Optional<Comment> existingComment = commentRepository.findById(id);

        if(!existingComment.isPresent()) {
            return null;
        }

        Comment updatedComment = existingComment.get();
        updatedComment.setContent(commentRequest.getContent());
        updatedComment.setApproved(commentRequest.isApproved());
        updatedComment.setUpdatedAt(LocalDateTime.now());

        if(commentRequest.getParentCommentId() != null){
            Optional<Comment> existingParentComment = commentRepository.findById(commentRequest.getParentCommentId());
            if(!existingParentComment.isPresent()){
                throw new RuntimeException("Parent comment not found");
            }
            updatedComment.setParentComment(existingParentComment.get());
        }

        if(commentRequest.getAuthorId() != null){
            Optional<BlogUser> existingAuthor = userService.getUserById(commentRequest.getAuthorId());
            if(!existingAuthor.isPresent()){
                throw new RuntimeException("Author not found");
            }
            updatedComment.setAuthor(existingAuthor.get());
        }


        return commentRepository.save(updatedComment);
    }

    public void deleteComment(Integer id){

        if(!commentRepository.existsById(id)){
            throw new RuntimeException("Commentário não encontrado");
        }

        commentRepository.deleteById(id);
    }


}
