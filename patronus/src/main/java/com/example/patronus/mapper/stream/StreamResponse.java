package com.example.patronus.mapper.stream;

import java.time.LocalDateTime;

public record StreamResponse(String id, String caption, String thumbnailUrl,
                             StreamResponse.UserResponse user,
                             LocalDateTime created, Integer like
) {
    public record UserResponse(String username, String imageUrl) {

    }

}