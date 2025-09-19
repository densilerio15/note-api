package com.example.notesapi.controller;

import com.example.notesapi.exception.NoteNotFoundException;
import com.example.notesapi.model.Note;
import com.example.notesapi.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = "*")
@Tag(name = "Notes", description = "API for managing notes")
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @Operation(summary = "Create a new note", description = "Creates a new note with title and body")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Note created successfully",
                content = @Content(schema = @Schema(implementation = Note.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(schema = @Schema(implementation = com.example.notesapi.exception.GlobalExceptionHandler.ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {
        Note createdNote = noteService.createNote(note);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Get all notes", description = "Retrieves all notes in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all notes",
                content = @Content(schema = @Schema(implementation = Note.class)))
    })
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }
    
    @Operation(summary = "Get note by ID", description = "Retrieves a specific note by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Note found",
                content = @Content(schema = @Schema(implementation = Note.class))),
        @ApiResponse(responseCode = "404", description = "Note not found",
                content = @Content(schema = @Schema(implementation = com.example.notesapi.exception.GlobalExceptionHandler.ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(
            @Parameter(description = "ID of the note to retrieve", example = "1")
            @PathVariable Long id) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent()) {
            return new ResponseEntity<>(note.get(), HttpStatus.OK);
        } else {
            throw new NoteNotFoundException("Note not found with id: " + id);
        }
    }
    
    @Operation(summary = "Update a note", description = "Updates an existing note with new title and body")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Note updated successfully",
                content = @Content(schema = @Schema(implementation = Note.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(schema = @Schema(implementation = com.example.notesapi.exception.GlobalExceptionHandler.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Note not found",
                content = @Content(schema = @Schema(implementation = com.example.notesapi.exception.GlobalExceptionHandler.ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @Parameter(description = "ID of the note to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Note noteDetails) {
        Optional<Note> updatedNote = noteService.updateNote(id, noteDetails);
        if (updatedNote.isPresent()) {
            return new ResponseEntity<>(updatedNote.get(), HttpStatus.OK);
        } else {
            throw new NoteNotFoundException("Note not found with id: " + id);
        }
    }
    
    @Operation(summary = "Delete a note", description = "Deletes a note by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Note deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Note not found",
                content = @Content(schema = @Schema(implementation = com.example.notesapi.exception.GlobalExceptionHandler.ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(
            @Parameter(description = "ID of the note to delete", example = "1")
            @PathVariable Long id) {
        boolean deleted = noteService.deleteNote(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NoteNotFoundException("Note not found with id: " + id);
        }
    }
}
