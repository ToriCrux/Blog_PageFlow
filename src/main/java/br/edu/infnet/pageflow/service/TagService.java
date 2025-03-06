package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.model.Category;
import br.edu.infnet.pageflow.model.Tag;
import br.edu.infnet.pageflow.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public Collection<Tag> getTags() {
        return tagRepository.getAllTags(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteTag(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag não encontrada!");
        }
        tagRepository.deleteById(id);
    }

    public Tag updateTag(UUID id, Tag tag) {
        Optional<Tag> existingTag = tagRepository.findById(id);
        if (existingTag.isPresent()) {
            Tag updatedTag = existingTag.get();
            updatedTag.setName(tag.getName());
            return tagRepository.save(updatedTag);
        }
        return null;
    }
}
