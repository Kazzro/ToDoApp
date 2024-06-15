package de.inmediasp.TodoApp.service;

import de.inmediasp.TodoApp.dao.NoteRepository;
import de.inmediasp.TodoApp.dao.UserRepository;
import de.inmediasp.TodoApp.entity.Note;
import de.inmediasp.TodoApp.entity.User;
import de.inmediasp.TodoApp.rest.payload.request.NewNoteRequest;
import de.inmediasp.TodoApp.rest.payload.request.UpdateNoteRequest;
import de.inmediasp.TodoApp.rest.payload.response.NoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NoteService {

    @Autowired
    private NoteRepository repository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    public List<Note> findAll() {
        return repository.findAll();
    }

    public List<NoteResponse> getAllNotes() {
        Optional<User> result = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        User theUser;
        List<NoteResponse> response;

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("");
        }

        theUser = result.get();
        response = Stream.concat(theUser.getReceivedNotes().stream(),
                        theUser.getPersonalNotes().stream())
                .sorted(Comparator.comparing(Note::getCreatedAt))
                .map(NoteResponse::new)
                .collect(Collectors.toList());
        return response;
    }

    public ResponseEntity<List<NoteResponse>> getReceivedNotes() {
        Optional<User> response = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return response.map(user -> new ResponseEntity<>(user.getReceivedNotes().stream().map(NoteResponse::new).collect(Collectors.toList()), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<NoteResponse>> getPersonalNotes() {
        Optional<User> response = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return response.map(user -> new ResponseEntity<>(user.getPersonalNotes().stream().map(NoteResponse::new).collect(Collectors.toList()), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    public Note findById(UUID theId) {
        Optional<Note> response = repository.findById(theId);
        if (response.isEmpty()) {
            throw new RuntimeException("No Note with ID " + theId);
        }
        return response.get();
    }

    public NoteResponse createNote(NewNoteRequest theRequest) {
        Note theNote;
        User theAuthor = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (theRequest.getCorrespondent() != null)
            theNote = new Note(theAuthor, theRequest, userService.findByUsername(theRequest.getCorrespondent()));
        else
            theNote = new Note(theAuthor, theRequest);
        repository.save(theNote);
        return new NoteResponse(theNote);
    }

    public NoteResponse updateNote(UUID noteId, UpdateNoteRequest theRequest) {
        Note theNote;
        Optional<Note> result = repository.findById(noteId);
        if(result.isEmpty()){
            throw new RuntimeException("Note not found");
        }

        theNote = result.get();
        theNote.setContent(theRequest.getContent());
        if (theRequest.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            theNote.setTitle(theRequest.getTitle());
            if (theRequest.getCorrespondent() != null)
                theNote.setTarget(userService.findByUsername(theRequest.getCorrespondent()));
        }
        return new NoteResponse(repository.save(theNote));
    }


    public Note save(Note theEntity) {
        return repository.save(theEntity);
    }


    public HttpStatus deleteById(UUID theId) {

        User requestGiver = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Note> result = repository.findById(theId);
        Note theNote;

        if(result.isEmpty()){
            return HttpStatus.NOT_FOUND;
        }

        theNote = result.get();
        if(!theNote.getAuthor().equals(requestGiver)){
            return HttpStatus.FORBIDDEN;
        }
        repository.deleteById(theId);
        return HttpStatus.OK;

    }
}
