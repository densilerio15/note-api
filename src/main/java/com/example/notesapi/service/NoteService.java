package com.example.notesapi.service;

import com.example.notesapi.model.Note;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NoteService {
    
    private final Map<Long, Note> notes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public List<Note> getAllNotes() {
        return new ArrayList<>(notes.values());
    }
    
    public Optional<Note> getNoteById(Long id) {
        return Optional.ofNullable(notes.get(id));
    }
    
    public Note createNote(Note note) {
        note.setId(idGenerator.getAndIncrement());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        notes.put(note.getId(), note);
        return note;
    }
    
    public Optional<Note> updateNote(Long id, Note noteDetails) {
        Note existingNote = notes.get(id);
        if (existingNote != null) {
            existingNote.setTitle(noteDetails.getTitle());
            existingNote.setBody(noteDetails.getBody());
            existingNote.setUpdatedAt(LocalDateTime.now());
            return Optional.of(existingNote);
        }
        return Optional.empty();
    }
    
    public boolean deleteNote(Long id) {
        return notes.remove(id) != null;
    }
}
