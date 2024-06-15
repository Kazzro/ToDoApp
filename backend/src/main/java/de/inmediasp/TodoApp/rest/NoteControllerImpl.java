package de.inmediasp.TodoApp.rest;

import de.inmediasp.TodoApp.rest.payload.request.NewNoteRequest;
import de.inmediasp.TodoApp.rest.payload.request.UpdateNoteRequest;
import de.inmediasp.TodoApp.rest.payload.response.NoteResponse;
import de.inmediasp.TodoApp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/users/my")
@PreAuthorize("isAuthenticated()")
public class NoteControllerImpl implements NoteController{
    @Autowired
    NoteService noteService;

    @GetMapping("/notes")
    public ResponseEntity<List<NoteResponse>> getAllNotes() {
        return new ResponseEntity<>(noteService.getAllNotes(), HttpStatus.OK);
    }

    @GetMapping("/notes/received")
    public ResponseEntity<List<NoteResponse>> getAllReceivedNotes() {
        return noteService.getReceivedNotes();
    }

    @GetMapping("/notes/personal")
    public ResponseEntity<List<NoteResponse>> getAllPersonalNotes() {
        return noteService.getPersonalNotes();
    }

    @PostMapping("/notes")
    public ResponseEntity<NoteResponse> createNote(@RequestBody NewNoteRequest theRequest) {
        return new ResponseEntity<>(noteService.createNote(theRequest), HttpStatus.CREATED);
    }


    @PutMapping("/notes/{noteId}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable UUID noteId, @RequestBody UpdateNoteRequest theRequest) {
        return new ResponseEntity<>(noteService.updateNote(noteId, theRequest), HttpStatus.OK);
    }

    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<HttpStatus> deleteNote(@PathVariable UUID noteId) {
        return new ResponseEntity<>(noteService.deleteById(noteId));
    }


}
