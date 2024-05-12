package com.example.patronus.service;

import com.example.patronus.models.jpa.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
public interface StreamService {
    Stream save(Stream liveStream);
    Page<Stream> getStreams(UUID userId, Pageable pageable);
    Stream getByStreamId( UUID streamId);
    Page<Stream> getAllFiltered(UUID userId, Pageable pageable);
    Page<Stream> listAllStreamsByPage(Pageable pageable);
    void archiveStream(UUID streamId, UUID userId);
    Stream endStream(UUID streamId);
//    void uploadThumbnail(UUID streamId, MultipartFile poster);
}
