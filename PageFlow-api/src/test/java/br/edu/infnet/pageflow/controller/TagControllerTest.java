package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.TagRequest;
import br.edu.infnet.pageflow.entities.Tag;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TagController.class)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private TagService tagService;

    @Test
    void getAllTags() throws Exception {
        Tag tag = new Tag();
        tag.setName("Uma nova tag");

        List<Tag> tags = List.of(tag);

        when(tagService.getTags()).thenReturn(tags);

        mockMvc.perform(get("/api/v1/tags")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Uma nova tag"));
    }

    @Test
    void testCreateTag() throws Exception {
        Tag tag = new Tag();
        tag.setName("Uma nova tag");

        TagRequest tagRequest = new TagRequest();
        tagRequest.setName(tag.getName());

        when(tagService.createTag(tagRequest.getName())).thenReturn(tag);

        mockMvc.perform(post("/api/v1/tags/new")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Uma nova tag"));
    }

    @Test
    void testUpdateTag() throws Exception {

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("Uma nova tag editada");

        when(tagService.updateTag(eq(1), any(Tag.class))).thenReturn(tag);

        mockMvc.perform(put("/api/v1/tags/{id}", tag.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Uma nova tag editada"));
    }

    @Test
    void testDeleteTag() throws Exception {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("Uma nova tag");

        doNothing().when(tagService).deleteTag(tag.getId());

        mockMvc.perform(delete("/api/v1/tags/{id}", tag.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}