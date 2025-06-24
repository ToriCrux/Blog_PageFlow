package br.edu.infnet.pageflow;

import br.edu.infnet.pageflow.dto.CategoryRequest;
import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.dto.SignupRequest;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.CommentRepository;
import br.edu.infnet.pageflow.repository.PostCommentRelationRepository;
import br.edu.infnet.pageflow.repository.PostRepository;
import br.edu.infnet.pageflow.repository.UserRepository;
import br.edu.infnet.pageflow.service.*;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import br.edu.infnet.pageflow.utils.PostStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagService tagService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostCommentRelationRepository postCommentRelationRepository;

    protected InputStream getClassLoaderResourceAsStream() {
        return getClass().getClassLoader().getResourceAsStream("mockupData.txt");
    }

    @Override
    public void run(ApplicationArguments args) {

        try {
            InputStream inputStream = getClassLoaderResourceAsStream();

            if (inputStream == null) {
                throw new FileNotFoundException("Arquivo mockupData.txt não encontrado em resources.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String fileLine;

            BlogUser administrator = new BlogUser();
            administrator.setRole(BlogUserRoles.ADMINISTRATOR);
            administrator.setUsername("administrator");
            administrator.setPassword("administrator");
            administrator.setEmail("administrator@email.com");

            BlogUser author = new BlogUser();
            author.setRole(BlogUserRoles.AUTHOR);
            author.setUsername("author");
            author.setPassword("author");
            author.setEmail("author@email.com");

            while ((fileLine = reader.readLine()) != null) {
                String[] fields = fileLine.split(";");

                switch (fields[0]) {
                    case "author":
                        SignupRequest signupRequest = new SignupRequest();
                        signupRequest.setName(fields[2]);
                        signupRequest.setUsername(fields[3]);
                        signupRequest.setEmail(fields[4]);
                        signupRequest.setPassword(fields[5]);
                        signupRequest.setRole(BlogUserRoles.AUTHOR);

                        userService.createUser(signupRequest);

                        break;

                    case "category":

                        categoryService.createCategory(fields[2]);

                        break;

                    case "comment":
                        CommentRequest commentRequest = new CommentRequest();
                        commentRequest.setId(Integer.parseInt(fields[1]));
                        commentRequest.setContent(fields[2]);
                        commentRequest.setApproved(true);

                        commentService.createComment(commentRequest);
                        break;

                    case "post":
                        PostRequest postRequest = new PostRequest();
                        postRequest.setTitle(fields[2]);
                        postRequest.setContent(fields[3]);
                        postRequest.setAuthorId(Integer.valueOf(fields[4]));
                        postRequest.setCategoryId(Integer.valueOf(fields[5]));

                        if (Objects.equals(fields[6], "DRAFT")) {
                            postRequest.setStatus(PostStatus.DRAFT);

                        } else {
                            postRequest.setStatus(PostStatus.PUBLISHED);
                        };

                        postService.createPost(postRequest);
                        break;

                    case "postcommentrelation":

                        Optional<Post> post = postRepository.getPostById(Integer.valueOf(fields[2]));
                        Optional<Comment> comment = commentRepository.findById(Integer.valueOf(fields[3]));

                        if (post.isPresent() && comment.isPresent()) {
                            PostCommentRelation relation = new PostCommentRelation();
                            relation.setPost(post.get());
                            relation.setComment(comment.get());
                            postCommentRelationRepository.save(relation);
                        } else {
                            System.out.println("Relação ignorada: Post ou Comentário não encontrado para linha: " + Arrays.toString(fields));
                        }

                        break;

                    default:
                        System.out.println("Registro inválido: " + fields[0]);
                        break;
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }

        System.out.println("Dados adicionados com sucesso!");
    }
}
