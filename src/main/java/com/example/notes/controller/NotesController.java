package com.example.notes.controller;

import com.example.notes.model.Note;
import com.example.notes.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class NotesController {

    @Autowired
    private NotesService service;

    @GetMapping("/notes")
    public List<Note> getNotes(@RequestParam(required = false) String q,
                               @RequestParam(required = false) String tag) {
        return service.getNotes(q, tag);
    }

    @GetMapping("/notes/archived")
    public List<Note> getArchived() {
        return service.getArchived();
    }

    @PostMapping("/notes")
    public Note addNote(@RequestBody Map<String, String> body) {
        return service.addNote(body.get("text"), body.get("tags"));
    }

    @PutMapping("/notes/{id}")
    public Note update(@PathVariable Long id, @RequestBody Map<String, Object> body) {

        String text = (String) body.get("text");
        String tags = (String) body.get("tags");
        Boolean pinned = body.get("pinned") != null ?
                Boolean.valueOf(body.get("pinned").toString()) : null;

        return service.updateNote(id, text, tags, pinned)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @DeleteMapping("/notes/{id}")
    public Map<String, String> archive(@PathVariable Long id) {
        boolean ok = service.archive(id);
        return Collections.singletonMap("result", ok ? "archived" : "not found");
    }

    @PostMapping("/notes/{id}/restore")
    public Map<String, String> restore(@PathVariable Long id) {
        boolean ok = service.restore(id);
        return Collections.singletonMap("result", ok ? "restored" : "not found");
    }

    @DeleteMapping("/notes/{id}/hard")
    public Map<String, String> hard(@PathVariable Long id) {
        service.hardDelete(id);
        return Collections.singletonMap("result", "deleted");
    }
}
