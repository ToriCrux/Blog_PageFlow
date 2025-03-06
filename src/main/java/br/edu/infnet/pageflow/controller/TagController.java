package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.model.Category;
import br.edu.infnet.pageflow.model.Tag;
import br.edu.infnet.pageflow.service.CategoryService;
import br.edu.infnet.pageflow.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public ResponseEntity<Collection<Tag>> getAllTags() {
        return ResponseEntity.ok(tagService.getTags());
    }

    @PostMapping("/new")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(tag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable UUID id, @RequestBody Tag tag) {
        Tag updatedTag = tagService.updateTag(id, tag);
        return updatedTag != null
                ? ResponseEntity.ok(updatedTag)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

}