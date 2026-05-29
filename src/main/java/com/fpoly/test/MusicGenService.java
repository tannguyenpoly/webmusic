package com.fpoly.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MusicGenService {

	@Value("${musicapi.key}")
	private String apiKey;

	private final RestTemplate restTemplate = new RestTemplate();
	private final String BASE_URL = "https://api.musicapi.ai";

	public String createMusicJob(String prompt, String tags, String title, boolean instrumental) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		String body = String.format("""
				{
				  "custom_mode": true,
				  "mv": "sonic-v4",
				  "prompt": "%s",
				  "tags": "%s",
				  "title": "%s",
				  "make_instrumental": %b
				}
				""", prompt, tags, title, instrumental);

		log.info("=== GỌI MUSICAPI ===");
		log.info("API Key: {}", apiKey.substring(0, 8) + "..."); // log 8 ký tự đầu
		log.info("Body gửi đi: {}", body);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL + "/api/v1/sonic/create",
					new HttpEntity<>(body, headers), Map.class);
			log.info("Status: {}", response.getStatusCode());
			log.info("Response: {}", response.getBody());

			String taskId = (String) response.getBody().get("task_id");
			log.info("Task ID: {}", taskId);
			return taskId;

		} catch (Exception e) {
			log.error("Lỗi gọi API: {}", e.getMessage());
			throw e;
		}
	}

	public Map getTaskResult(String taskId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);

		ResponseEntity<Map> response = restTemplate.exchange(BASE_URL + "/api/v1/sonic/task/" + taskId, HttpMethod.GET,
				new HttpEntity<>(headers), Map.class);

		log.info("Poll response đầy đủ: {}", response.getBody()); // thêm dòng này
		return response.getBody();
	}

	public Map waitForResult(String taskId) throws InterruptedException {
		for (int i = 0; i < 36; i++) {
			Map result = getTaskResult(taskId);
			log.info("Poll {}: response = {}", i + 1, result);

			// Kiểm tra type=not_ready trước
			if ("not_ready".equals(result.get("type"))) {
				Thread.sleep(5000);
				continue;
			}

			// Lấy mảng data
			List<Map> dataList = (List<Map>) result.get("data");
			if (dataList != null && !dataList.isEmpty()) {
				// Kiểm tra tất cả clip đã xong chưa
				boolean allDone = dataList.stream()
						.allMatch(clip -> "succeeded".equals(clip.get("state")) || "failed".equals(clip.get("state")));

				if (allDone) {
					// Lấy clip đầu tiên succeeded
					Map succeededClip = dataList.stream().filter(clip -> "succeeded".equals(clip.get("state")))
							.findFirst().orElse(null);

					if (succeededClip != null) {
						log.info("Gen nhạc xong! audio_url: {}", succeededClip.get("audio_url"));
						return succeededClip; // trả về clip, có audio_url, title, v.v.
					} else {
						throw new RuntimeException("Tất cả clip đều failed");
					}
				}
			}

			Thread.sleep(5000);
		}
		throw new RuntimeException("Timeout sau 3 phút");
	}
}