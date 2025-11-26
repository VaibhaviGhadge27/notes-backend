package com.example.notes.repository;

import com.example.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByArchivedFalseOrderByPinnedDescIdDesc();

    List<Note> findByArchivedFalseAndTextContainingIgnoreCaseOrderByPinnedDescIdDesc(String text);

    List<Note> findByArchivedFalseAndTagsContainingIgnoreCaseOrderByPinnedDescIdDesc(String tags);

    List<Note> findByArchivedTrueOrderByIdDesc();
}
