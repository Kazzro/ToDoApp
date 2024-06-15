package de.inmediasp.TodoApp.rest.payload.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.inmediasp.TodoApp.entity.Note;
import de.inmediasp.TodoApp.rest.payload.response.pojo.Content;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class NoteResponse {
    private String id;
    private String author;
    private String correspondent;
    private String title;
    private List<Content> content;
    private static final Logger logger = LoggerFactory.getLogger(NoteResponse.class);

    public NoteResponse(Note note) {
        this.setId(note.getNote_ID().toString());
        this.setAuthor(note.getAuthor().getUsername());
        if (note.getTarget() != null) {
            this.setCorrespondent(note.getTarget().getUsername());
        }
        this.setTitle(note.getTitle());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonContent = note.getContent();

            if (jsonContent != null && !jsonContent.isEmpty()) {
                jsonContent = trimJsonContent(jsonContent);
                this.setContent(objectMapper.readValue(jsonContent, new TypeReference<>() {}));
            } else {
                this.setContent(new ArrayList<>());
            }
        } catch (IOException e) {
            logger.error("Error deserializing: " + e.getMessage());
            this.setContent(new ArrayList<>());
        }
    }

    private String trimJsonContent(String jsonContent){
        if (StringUtils.hasText(jsonContent)) {
            if(jsonContent.contains("\\"))
                jsonContent = jsonContent.replace("\\", "");
            if(jsonContent.startsWith("\"\""))
                jsonContent = jsonContent.substring(2);
            if(jsonContent.endsWith("\"\""))
                jsonContent = jsonContent.substring(0,jsonContent.length()-2);
            if(jsonContent.startsWith("\""))
                jsonContent = jsonContent.substring(1);
            if(jsonContent.endsWith("\""))
                jsonContent = jsonContent.substring(0,jsonContent.length()-1);

        }
        return jsonContent;
    }
}


