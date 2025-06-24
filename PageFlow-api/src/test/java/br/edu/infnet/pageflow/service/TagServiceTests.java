package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.Tag;
import br.edu.infnet.pageflow.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TagServiceTests {

    @Mock private TagRepository tagRepository;

    @InjectMocks private TagService tagService;

    private Tag tag;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        tag = new Tag();
        tag.setId(1);
        tag.setName("GIS");
    }

    @Test
    void testGetTags() {
        when(tagRepository.getAllTags(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(List.of(tag));

        Collection<Tag> tags = tagService.getTags();

        assertEquals(1, tags.size());
        assertEquals("GIS", tags.iterator().next().getName());
    }

    @Test
    void testCreateTag() {
        when(tagRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Tag created = tagService.createTag("GeoTI");

        assertEquals("GeoTI", created.getName());
    }

    @Test
    void testDeleteTagExists() {
        when(tagRepository.existsById(1)).thenReturn(true);
        tagService.deleteTag(1);
        verify(tagRepository).deleteById(1);
    }

    @Test
    void testDeleteTagNotFound() {
        when(tagRepository.existsById(99)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> tagService.deleteTag(99));
    }

    @Test
    void testUpdateTagExists() {
        Tag updatedTag = new Tag();
        updatedTag.setName("Updated");

        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Tag result = tagService.updateTag(1, updatedTag);

        assertEquals("Updated", result.getName());
    }

    @Test
    void testUpdateTagNotFound() {
        Tag updatedTag = new Tag();
        updatedTag.setName("404");

        when(tagRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tagService.updateTag(99, updatedTag));
    }
}
