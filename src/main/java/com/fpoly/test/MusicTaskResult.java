package com.fpoly.test;

import java.util.List;

import lombok.Data;

//MusicTaskResult.java
@Data
public class MusicTaskResult {
	private String task_id;
	private String state; // "pending" | "processing" | "succeeded" | "failed"
	private List<AudioData> data;

	@Data
	public static class AudioData {
		private String audio_url;
		private String title;
		private String duration;
	}
}
