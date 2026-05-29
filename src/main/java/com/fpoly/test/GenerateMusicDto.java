package com.fpoly.test;

import lombok.Data;

@Data
public class GenerateMusicDto {
	private String prompt;
	private String tags; // "lo-fi, chill, relaxing"
	private String title;
	private boolean instrumental;
}