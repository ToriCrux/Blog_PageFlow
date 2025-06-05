package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.Tag;
import br.edu.infnet.pageflow.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Collection<Tag> getTags() {
        return tagRepository.getAllTags(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Tag createTag(String tagName) {
        Tag newTag = new Tag();
        newTag.setName(tagName);
        return tagRepository.save(newTag);
    }

    public void deleteTag(Integer id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        tagRepository.deleteById(id);
    }

    public Tag updateTag(Integer id, Tag updatedtag) {

        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

        existingTag.setName(updatedtag.getName());

        return tagRepository.save(existingTag);

    }
}
