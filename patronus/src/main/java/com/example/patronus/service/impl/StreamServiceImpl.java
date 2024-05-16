package com.example.patronus.service.impl;

import com.example.patronus.exception.stream.StreamNotFoundException;
import com.example.patronus.repository.StreamRepository;
import com.example.patronus.service.StreamCacheService;
import com.example.patronus.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.patronus.models.jpa.Stream;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StreamServiceImpl implements StreamService {
    private final StreamRepository repository;
    private final StreamCacheService cacheService;

    @Override
    public Stream save(Stream liveStream) {
        return repository.save(liveStream);
    }

    @Override
    public Page<Stream> getAllFiltered(UUID userId, Pageable pageable) {
        return repository.findAll(userId, pageable);
    }

    @Override
    public void archiveStream(UUID streamId, UUID userId) {
        repository.updateByStreamIdAndUserId(streamId, userId);
    }

    @Override
    public Page<Stream> getStreams(UUID userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<Stream> listAllStreamsByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Stream getByStreamId(UUID streamId) {
        return repository.findById(streamId)
                .orElseThrow(() -> new StreamNotFoundException(streamId));
    }

    @Override
    public Stream endStream(UUID userId) {
        var live = repository.findLiveByUserId(userId)
                .orElseThrow(() -> new StreamNotFoundException(userId));
        live.setLive(false);
        cacheService.removeStream(live.getId());
        return repository.save(live);
    }

//    @Override
//    public void uploadThumbnail(UUID streamId, MultipartFile poster) {
//        LiveStream stream = repository.findById(streamId).orElseThrow();
//        String uploadedFile = storageService.uploadFile(poster);
//        stream.setThumbnailUrl(uploadedFile);
//        repository.save(stream);
//    }


}
