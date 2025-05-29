package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.TagRequest;
import br.edu.infnet.pageflow.entities.Tag;
import br.edu.infnet.pageflow.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<Collection<Tag>> getAllTags() {
        return ResponseEntity.ok(tagService.getTags());
    }

    @PostMapping("/new")
    public ResponseEntity<Tag> createTag(@RequestBody TagRequest tagRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(tagRequest.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Integer id,@RequestBody Tag tag) {

        Tag updatedTag = tagService.updateTag(id, tag);

        return updatedTag != null
                ? ResponseEntity.ok(updatedTag)
                : ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

}