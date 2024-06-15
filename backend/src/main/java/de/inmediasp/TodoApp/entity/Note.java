package de.inmediasp.TodoApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.inmediasp.TodoApp.rest.payload.request.NewNoteRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "notes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pk_note_id")
    private UUID note_ID;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @JsonIgnoreProperties({"personalNotes", "receivedNotes", "password", "roles"})
    @ManyToOne
    @JoinColumn(name = "fk_author_user_id")
    private User author;

    @JsonIgnoreProperties({"personalNotes", "receivedNotes", "password", "roles"})
    @ManyToOne
    @JoinColumn(name = "fk_target")
    private User target;

    public Note(Date createdAt, String title, String content) {
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
    }

    public Note(User author, NewNoteRequest theRequest) {
        ObjectMapper mapper = new ObjectMapper();
        this.author = author;
        this.createdAt = new Date();
        this.title = theRequest.getTitle();
        try {
            this.content = mapper.writeValueAsString(theRequest.getContent());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Note(User author, NewNoteRequest theRequest, User theTarget) {
        this (author, theRequest);
        this.target = theTarget;
    }

}
