package com.blog.api.service;

import com.blog.api.dto.request.ReactionRequest;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.types.ReactionTableType;
import com.blog.api.types.ReactionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReactionService {
    public ReactionResponse getById(String id);
    public void toggleReaction(ReactionRequest request);
    public List<ReactionResponse> getAllByReactionTableId(ReactionTableType type, String id);
    public List<ReactionResponse> getAll();
    public Integer countOfReactionByReactionTableId(String id);
    public Integer countOfReaction();
    public ReactionType checkReaction(ReactionTableType type , String reactionTableId, String userId);
}
