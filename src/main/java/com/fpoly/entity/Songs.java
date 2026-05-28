package com.fpoly.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Setter
@Getter
@Table(name = "songs")
public class Songs{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

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

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
}