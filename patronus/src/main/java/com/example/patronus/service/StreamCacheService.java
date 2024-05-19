package com.example.patronus.service;


import com.example.patronus.models.entity.Stream;
import com.example.patronus.models.redis.StreamHash;
import com.example.patronus.payload.request.StreamPatchRequest;
import com.example.patronus.payload.request.StreamPutRequest;

import java.util.List;
import java.util.UUID;

public interface StreamCacheService {

    StreamHash save(Stream liveStream);
    StreamHash save(UUID id, StreamPutRequest liveStream);
    List<StreamHash> getAllLiveStreams();
    StreamHash updateStreamInfo(UUID id, StreamPatchRequest status);
    StreamHash updateIngressInfo(UUID id, StreamPutRequest status);
    StreamHash getLiveByUserId(UUID userId);
    void removeStream(UUID userId);
    StreamHash getById(UUID streamId);
}

