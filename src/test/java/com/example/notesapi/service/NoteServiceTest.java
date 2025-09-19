package com.example.notesapi.service;

import com.example.notesapi.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @InjectMocks
    private NoteService noteService;

    private Note testNote;

    @BeforeEach
    void setUp() {
        testNote = new Note("Test Title", "Test Body");
    }

    @Test
    void createNote_ShouldCreateNoteWithGeneratedId() {
        // When
        Note createdNote = noteService.createNote(testNote);

        // Then
        assertNotNull(createdNote.getId());
        assertEquals("Test Title", createdNote.getTitle());
        assertEquals("Test Body", createdNote.getBody());
        assertNotNull(createdNote.getCreatedAt());
        assertNotNull(createdNote.getUpdatedAt());
    }

    @Test
    void createNote_ShouldIncrementIdForMultipleNotes() {
        // When
        Note firstNote = noteService.createNote(new Note("First", "Body"));
        Note secondNote = noteService.createNote(new Note("Second", "Body"));

        // Then
        assertNotEquals(firstNote.getId(), secondNote.getId());
        assertTrue(secondNote.getId() > firstNote.getId());
    }

    @Test
    void getAllNotes_WhenEmpty_ShouldReturnEmptyList() {
        // When
        List<Note> notes = noteService.getAllNotes();

        // Then
        assertTrue(notes.isEmpty());
    }

    @Test
    void getAllNotes_WhenNotesExist_ShouldReturnAllNotes() {
        // Given
        Note note1 = noteService.createNote(new Note("Title 1", "Body 1"));
        Note note2 = noteService.createNote(new Note("Title 2", "Body 2"));

        // When
        List<Note> notes = noteService.getAllNotes();

        // Then
        assertEquals(2, notes.size());
        assertTrue(notes.contains(note1));
        assertTrue(notes.contains(note2));
    }

    @Test
    void getNoteById_WhenNoteExists_ShouldReturnNote() {
        // Given
        Note createdNote = noteService.createNote(testNote);
        Long noteId = createdNote.getId();

        // When
        Optional<Note> foundNote = noteService.getNoteById(noteId);

        // Then
        assertTrue(foundNote.isPresent());
        assertEquals(createdNote.getId(), foundNote.get().getId());
        assertEquals("Test Title", foundNote.get().getTitle());
        assertEquals("Test Body", foundNote.get().getBody());
    }

    @Test
    void getNoteById_WhenNoteNotExists_ShouldReturnEmpty() {
        // When
        Optional<Note> foundNote = noteService.getNoteById(999L);

        // Then
        assertFalse(foundNote.isPresent());
    }

    @Test
    void updateNote_WhenNoteExists_ShouldUpdateAndReturnNote() {
        // Given
        Note createdNote = noteService.createNote(testNote);
        Long noteId = createdNote.getId();
        LocalDateTime originalCreatedAt = createdNote.getCreatedAt();
        
        Note updateDetails = new Note("Updated Title", "Updated Body");

        // When
        Optional<Note> updatedNote = noteService.updateNote(noteId, updateDetails);

        // Then
        assertTrue(updatedNote.isPresent());
        assertEquals(noteId, updatedNote.get().getId());
        assertEquals("Updated Title", updatedNote.get().getTitle());
        assertEquals("Updated Body", updatedNote.get().getBody());
        assertEquals(originalCreatedAt, updatedNote.get().getCreatedAt());
        assertTrue(updatedNote.get().getUpdatedAt().isAfter(originalCreatedAt));
    }

    @Test
    void updateNote_WhenNoteNotExists_ShouldReturnEmpty() {
        // Given
        Note updateDetails = new Note("Updated Title", "Updated Body");

        // When
        Optional<Note> updatedNote = noteService.updateNote(999L, updateDetails);

        // Then
        assertFalse(updatedNote.isPresent());
    }

    @Test
    void deleteNote_WhenNoteExists_ShouldReturnTrue() {
        // Given
        Note createdNote = noteService.createNote(testNote);
        Long noteId = createdNote.getId();

        // When
        boolean deleted = noteService.deleteNote(noteId);

        // Then
        assertTrue(deleted);
        assertFalse(noteService.getNoteById(noteId).isPresent());
    }

    @Test
    void deleteNote_WhenNoteNotExists_ShouldReturnFalse() {
        // When
        boolean deleted = noteService.deleteNote(999L);

        // Then
        assertFalse(deleted);
    }

    @Test
    void updateNote_ShouldUpdateOnlyUpdatedAtTimestamp() {
        // Given
        Note createdNote = noteService.createNote(testNote);
        Long noteId = createdNote.getId();
        LocalDateTime originalCreatedAt = createdNote.getCreatedAt();
        
        // Wait a small amount to ensure timestamp difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Note updateDetails = new Note("Updated Title", "Updated Body");

        // When
        Optional<Note> updatedNote = noteService.updateNote(noteId, updateDetails);

        // Then
        assertTrue(updatedNote.isPresent());
        assertEquals(originalCreatedAt, updatedNote.get().getCreatedAt());
        assertTrue(updatedNote.get().getUpdatedAt().isAfter(originalCreatedAt));
        
        // Verify updatedAt is more recent than createdAt
        assertTrue(updatedNote.get().getUpdatedAt().isAfter(updatedNote.get().getCreatedAt()));
    }
}
