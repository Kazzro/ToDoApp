package de.inmediasp.TodoApp.rest.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateNoteRequest {
    private String id;
    private String author;
    private String correspondent;
    private String title;
    private String content;
}
