package com.fpoly.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MusicGenRequest {
    private boolean custom_mode;
    private String mv;          // "sonic-v4"
    private String prompt;
    private String tags;        // genre, mood
    private String title;
    private boolean make_instrumental;
}