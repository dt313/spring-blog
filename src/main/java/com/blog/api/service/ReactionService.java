package com.blog.api.service;

import com.blog.api.dto.request.ReactionRequest;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReactionService {
    public ReactionResponse getById(String id);
    public ReactionType toggleReaction(ReactionRequest request);
    public List<ReactionResponse> getAllByReactionTableId(TableType type, String id);
    public List<ReactionResponse> getAll();
    public Integer countOfReactionByReactionTableId(String id);
    public Integer countOfReaction();
    public ReactionType checkReaction(TableType type , String reactionTableId, String userId);
}
