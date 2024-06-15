package de.inmediasp.TodoApp.dao;

import de.inmediasp.TodoApp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findByAuthorUuid(UUID UserId);

}