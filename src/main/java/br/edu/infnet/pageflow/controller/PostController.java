package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping(value = "/")
    public String postList(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts";

    }
}
