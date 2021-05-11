package com.tutorial.events;

import com.tutorial.common.RestDocsConfiguration;
import com.tutorial.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest // Register all beans to test. We can use @WebMvcTest to test as slice test. But this way is easy.
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

    @Autowired // Enable to test cases because web server doesn't work
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 입력")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2010, 11, 23, 14, 23))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 30, 14, 23))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 5, 14, 30))
                .endEventDateTime(LocalDateTime.of(2018, 12, 6, 14, 30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("D Start up Factory")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(event)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("id").exists())
                    .andExpect(header().exists(HttpHeaders.LOCATION))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("free").value(false))
                    .andExpect(jsonPath("offline").value(true))
                    .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                    .andExpect(jsonPath("_links.self").exists())
                    .andExpect(jsonPath("_links.query-events").exists())
                    .andExpect(jsonPath("_links.update-events").exists())
                .andDo(document("create-event"))
        ;
    }

    @Test
    @TestDescription("입력받을 수 없는 값을 사용할 때")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2010, 11, 23, 14, 23))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 30, 14, 23))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 5, 14, 30))
                .endEventDateTime(LocalDateTime.of(2018, 12, 6, 14, 30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("D Start up Factory")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있을 때")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("잘못된 입력값이 입력됬을 때")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2010, 11, 23, 14, 23))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 21, 14, 23))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 24, 14, 30))
                .endEventDateTime(LocalDateTime.of(2018, 12, 6, 14, 30))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("D Start up Factory")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }
}