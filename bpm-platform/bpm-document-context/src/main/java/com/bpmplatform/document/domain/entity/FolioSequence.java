package com.bpmplatform.document.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "folio_sequences")
public class FolioSequence extends com.bpmplatform.common.domain.Entity<UUID> {

    @Column(nullable = false, unique = true)
    private String format;

    @Column(name = "current_seq", nullable = false)
    private long currentSeq = 0;

    @Column(nullable = false)
    private int year;

    public FolioSequence() {}

    public FolioSequence(UUID id, String format, int year) {
        super(id);
        this.format = format;
        this.year = year;
    }

    public String getFormat() { return format; }
    public long getCurrentSeq() { return currentSeq; }
    public int getYear() { return year; }

    public String nextFolio() {
        currentSeq++;
        return String.format(format, year, currentSeq);
    }
}
