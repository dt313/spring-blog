package com.blog.api.mapper;

import com.blog.api.dto.request.ReactionRequest;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.entities.Reaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    @Mapping(target = "reactedUser" , ignore = true)
    Reaction toReaction(ReactionRequest request);
    @Mapping(target = "reactedUser" , ignore = true)
    ReactionResponse toReactionResponse(Reaction reaction);

    @Mapping(target = "reactedUser" , ignore = true)
    void mappingReaction(@MappingTarget Reaction reaction, ReactionRequest request);
}
