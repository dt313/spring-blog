package com.blog.api.service;

import com.blog.api.dto.request.ReactionRequest;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReactionService {
    ReactionResponse getById(Long id);
    ReactionResponse toggleReaction(ReactionRequest request);
    List<ReactionResponse> getAllByReactionTableId(TableType type, Long id);
    List<ReactionResponse> getAll();
    Integer countOfReactionByReactionTableId(Long id);
    Integer countOfReaction();
    ReactionType checkReaction(TableType type, Long reactionTableId, Long userId);
}
