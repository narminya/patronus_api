package com.example.patronus.payload.response;

public record LiveStreamResponse(String id, String caption,
                                 String ingressId,
                                 String serverUrl, String streamKey, Boolean chatDelayed, Boolean chatEnabled,
                                 Boolean chatFollowersOnly, String userId, String username, String name) {


}