package de.inmediasp.TodoApp.rest.payload.response.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    String task;
    boolean status;
}
