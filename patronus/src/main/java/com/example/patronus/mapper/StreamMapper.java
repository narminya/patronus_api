package com.example.patronus.mapper;

import com.example.patronus.mapper.stream.StreamResponse;
import com.example.patronus.models.entity.Stream;
import com.example.patronus.models.redis.StreamHash;
import com.example.patronus.payload.response.LiveStreamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StreamMapper {
    @Mapping(source = "user", target = "user")
    StreamResponse mapToStreamResponse(Stream liveStream);
    List<StreamResponse> mapToStreamResponses(List<Stream> liveStreams);
    LiveStreamResponse mapToLiveStreamResponse(StreamHash hash);
}