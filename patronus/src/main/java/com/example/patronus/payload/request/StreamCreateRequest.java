package com.example.patronus.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamCreateRequest {
    private String caption;
    private String thumbnailUrl;

}
