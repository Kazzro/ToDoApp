package de.inmediasp.TodoApp.rest;

import de.inmediasp.TodoApp.rest.payload.request.NewNoteRequest;
import de.inmediasp.TodoApp.rest.payload.request.UpdateNoteRequest;
import de.inmediasp.TodoApp.rest.payload.response.NoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface NoteController {

    ResponseEntity<List<NoteResponse>> getAllNotes();
    ResponseEntity<List<NoteResponse>> getAllReceivedNotes();
    ResponseEntity<List<NoteResponse>> getAllPersonalNotes();
    ResponseEntity<NoteResponse> createNote(@RequestBody NewNoteRequest theRequest);
    ResponseEntity<NoteResponse> updateNote(@PathVariable UUID noteId, @RequestBody UpdateNoteRequest theRequest);
    ResponseEntity<HttpStatus> deleteNote(@PathVariable UUID noteId);
}
