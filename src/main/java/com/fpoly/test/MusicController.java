package com.fpoly.test;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/music")
@Slf4j
public class MusicController {

	private final MusicGenService musicGenService;

	public MusicController(MusicGenService musicGenService) {
		this.musicGenService = musicGenService;
	}

	@PostMapping("/generate")
	public ResponseEntity<?> generate(@RequestBody GenerateMusicDto dto) {
		try {
			String taskId = musicGenService.createMusicJob(dto.getPrompt(), dto.getTags(), dto.getTitle(),
					dto.isInstrumental());

			Map result = musicGenService.waitForResult(taskId);

			return ResponseEntity.ok(result); // trả thẳng Map về, không cần parse
		} catch (Exception e) {
			log.error("Lỗi gen nhạc: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
		}
	}
}
