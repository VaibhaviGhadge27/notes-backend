package com.example.notes.service;

import com.example.notes.model.Note;
import com.example.notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class NotesService {

    @Autowired
    private NoteRepository repository;

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public List<Note> getNotes(String q, String tag) {
        if (q != null && !q.isBlank()) {
            return repository.findByArchivedFalseAndTextContainingIgnoreCaseOrderByPinnedDescIdDesc(q);
        }
        if (tag != null && !tag.isBlank()) {
            return repository.findByArchivedFalseAndTagsContainingIgnoreCaseOrderByPinnedDescIdDesc(tag);
        }
        return repository.findByArchivedFalseOrderByPinnedDescIdDesc();
    }

    public Note addNote(String text, String tags) {
        Note n = new Note();
        n.setText(text);
        n.setTags(tags == null ? "" : tags);
        n.setTimestamp(now());
        n.setPinned(false);
        n.setArchived(false);
        return repository.save(n);
    }

    public Optional<Note> updateNote(Long id, String text, String tags, Boolean pinned) {
        Optional<Note> opt = repository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        Note n = opt.get();
        if (text != null) n.setText(text);
        if (tags != null) n.setTags(tags);
        if (pinned != null) n.setPinned(pinned);

        n.setTimestamp(now());
        repository.save(n);

        return Optional.of(n);
    }

    public boolean archive(Long id) {
        Optional<Note> opt = repository.findById(id);
        if (opt.isEmpty()) return false;

        Note n = opt.get();
        n.setArchived(true);
        repository.save(n);
        return true;
    }

    public boolean restore(Long id) {
        Optional<Note> opt = repository.findById(id);
        if (opt.isEmpty()) return false;

        Note n = opt.get();
        n.setArchived(false);
        repository.save(n);
        return true;
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }

    public List<Note> getArchived() {
        return repository.findByArchivedTrueOrderByIdDesc();
    }
}
