package de.inmediasp.TodoApp.rest.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewNoteRequest {
    String title;
    String content;
    String correspondent;
}
