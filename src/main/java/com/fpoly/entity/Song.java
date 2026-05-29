package com.fpoly.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "prompt", columnDefinition = "NVARCHAR(MAX)")
    private String prompt;

    @Column(name = "audio_url", length = 255)
    private String audioUrl;

    @Column(name = "cover_url", length = 255)
    private String coverUrl;

    @Column(name = "duration_sec")
    private Integer durationSec;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "token_cost")
    private Integer tokenCost;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "status", length = 20)
    private String status = "PROCESSING";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}