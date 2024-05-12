package com.example.patronus.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamPatchRequest {

    private String name;
    private Boolean chatEnabled;
    private Boolean chatFollowersOnly;
    private Boolean chatDelayed;

}