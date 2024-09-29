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
import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.parameters.P;
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
    public ReactionResponse getById(Long id) {
        Reaction reaction = reactionRepository.findById(id).orElseThrow(() ->
            new AppException(ErrorCode.REACTION_NOT_FOUND)
        );

        ReactionResponse response = reactionMapper.toReactionResponse(reaction);
        response.setReactedUser(userMapper.toBasicUserResponse(reaction.getReactedUser()));

        return response;
    }

    @Override
    public ReactionResponse toggleReaction(ReactionRequest request) {
        System.out.println(request);
        checkReactionTable(request.getReactionTableType(), request.getReactionTableId());
        User reactedUser = userRepository.findById(request.getReactedUser()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        Reaction reaction = reactionRepository.findByReactionTableIdAndReactedUser(
                request.getReactionTableId(), reactedUser);


        if(Objects.isNull(reaction) ) {

            if(request.getType().equals(ReactionType.NULL)) {
                return null;
            }
            // create reaction
            reaction = reactionMapper.toReaction(request);
            reaction.setReactedUser(reactedUser);
            reactionRepository.save(reaction);

            ReactionResponse reactionResponse = reactionMapper.toReactionResponse(reaction);
            reactionResponse.setReactedUser(userMapper.toBasicUserResponse(reactedUser));
            return reactionResponse;

        }else {
            if(!request.getType().equals(ReactionType.NULL)) {
                reaction.setType(request.getType());
                reactionRepository.save(reaction);

                ReactionResponse reactionResponse = reactionMapper.toReactionResponse(reaction);
                reactionResponse.setReactedUser(userMapper.toBasicUserResponse(reactedUser));
                // remove reaction
                return reactionResponse;
            }else {
                reactionRepository.deleteById(reaction.getId());
                return null;

            }
        }
    }

    @Override
    public List<ReactionResponse> getAllByReactionTableId(TableType type,Long id) {
        boolean isExists = checkReactionTable(type,id);
        if(isExists) {
            List<Reaction> reactions = reactionRepository.findAllByReactionTableId(id);

            return reactions.stream().map((reaction) -> {
                ReactionResponse reactionResponse = reactionMapper.toReactionResponse(reaction);
                reactionResponse.setReactedUser(userMapper.toBasicUserResponse(reaction.getReactedUser()));
                return reactionResponse;
            }).toList();

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
    public Integer countOfReactionByReactionTableId(Long id) {
        List<Reaction> reactions = reactionRepository.findAllByReactionTableId(id);
        return reactions.size();
    }

    @Override
    public ReactionType checkReaction(TableType type, Long reactionTableId, Long userId) {
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

    private boolean checkReactionTable(TableType type, Long id) {
        switch (type){
            case TableType.ARTICLE:
                articleRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND)
                );
                return true;

            case TableType.COMMENT:
                commentRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.COMMENT_NOT_FOUND)
                );
                return true;
            default:
                throw new AppException(ErrorCode.REACTION_NO_TYPE_TABLE);
        }
    }


}
