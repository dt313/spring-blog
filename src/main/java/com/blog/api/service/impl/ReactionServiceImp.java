package com.blog.api.service.impl;

import com.blog.api.dto.request.ReactionRequest;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.entities.Reaction;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.ReactionMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.ReactionRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.ReactionService;
import com.blog.api.types.ReactionTableType;
import com.blog.api.types.ReactionType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionServiceImp implements ReactionService {
    ReactionRepository reactionRepository;
    ArticleRepository articleRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;

    UserMapper userMapper;
    ReactionMapper reactionMapper;
    @Override
    public ReactionResponse getById(String id) {
        Reaction reaction = reactionRepository.findById(id).orElseThrow(() ->
            new AppException(ErrorCode.REACTION_NOT_FOUND)
        );
        ReactionResponse response = reactionMapper.toReactionResponse(reaction);
        response.setReactedUser(userMapper.toBasicUserResponse(reaction.getReactedUser()));

        return response;
    }

    @Override
    public void toggleReaction(ReactionRequest request) {
        boolean isExistsReactionTable = checkReactionTable(request.getReactionTableType(), request.getReactionTableId());
        if(!isExistsReactionTable) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }
        User reactedUser = userRepository.findById(request.getReactedUser()).orElseThrow(
                () -> new AppException(ErrorCode.USER_EXISTS));

        Reaction reaction = reactionRepository.findByReactionTableIdAndReactedUser(
                request.getReactionTableId(), reactedUser);

        if(Objects.isNull(reaction)) {
            // create reaction
            reaction = reactionMapper.toReaction(request);
            reaction.setReactedUser(reactedUser);
            reactionRepository.save(reaction);
            return;

        }else {
            if(reaction.getType().toString().equals(request.getType().toString())) {
                // remove reaction
                reactionRepository.deleteById(reaction.getId());
                return;
            }
            // mapping reaction
            reactionMapper.mappingReaction(reaction,request);
            System.out.println(reaction);
            reaction.setReactedUser(reactedUser);
            reactionRepository.save(reaction);
            return;

        }
    }

    @Override
    public List<ReactionResponse> getAllByReactionTableId(ReactionTableType type,String id) {
        boolean isExists = checkReactionTable(type,id);
        if(isExists) {
            List<Reaction> reactions = reactionRepository.findAllByReactionTableId(id);
            List<ReactionResponse> response = reactions.stream().map((reaction) -> {
                ReactionResponse reactionResponse = reactionMapper.toReactionResponse(reaction);
                reactionResponse.setReactedUser(userMapper.toBasicUserResponse(reaction.getReactedUser()));
                return reactionResponse;
            }).toList();

            return response;
        }else throw new AppException(ErrorCode.REACTION_NOT_FOUND);
    }

    @Override
    public List<ReactionResponse> getAll() {
        List<Reaction> reactions = reactionRepository.findAll();
        List<ReactionResponse> response = reactions.stream().map((reaction) -> {
            ReactionResponse reactionResponse = reactionMapper.toReactionResponse(reaction);
            reactionResponse.setReactedUser(userMapper.toBasicUserResponse(reaction.getReactedUser()));
            return reactionResponse;
        }).toList();

        return response;
    }

    @Override
    public Integer countOfReaction() {
        List<Reaction> reactions = reactionRepository.findAll();
        return reactions.size();
    }

    @Override
    public Integer countOfReactionByReactionTableId(String id) {
        List<Reaction> reactions = reactionRepository.findAllByReactionTableId(id);
        return reactions.size();
    }

    @Override
    public ReactionType checkReaction(ReactionTableType type, String reactionTableId, String userId) {
        boolean isExists = checkReactionTable(type,reactionTableId);
        if(!isExists) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        User reactedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_EXISTS));

        Reaction reaction = reactionRepository.findByReactionTableIdAndReactedUser(reactionTableId, reactedUser);
        if(Objects.isNull(reaction)) {
            return ReactionType.NULL;
        }
        return reaction.getType();
    }

    private boolean checkReactionTable(ReactionTableType type, String id) {
        switch (type.toString()){
            case "ARTICLE":
                articleRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND)
                );
                return true;
            case "QUESTION":
                return false;

            case "ARTICLE_COMMENT":
                commentRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.COMMENT_NOT_FOUND)
                );
                return true;

            case "QUESION_COMMENT":
                return false;
            default:
                return false;
        }
    }


}
