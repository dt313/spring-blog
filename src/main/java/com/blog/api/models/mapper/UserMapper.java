package com.blog.api.models.mapper;

import com.blog.api.models.dto.UserDTO;
import com.blog.api.models.entity.User;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        UserDTO temp = new UserDTO();
        temp.setId(user.getId());
        temp.setUsername(user.getUsername());
        temp.setAge(user.getAge());
        temp.setFullName(user.getFullname());
        temp.setEmail(user.getEmail());

        temp.setAvatar(user.getAvatar());
        temp.setBirth(user.getBirth());
        temp.setGender(user.getGender());
        temp.setLinkBio(user.getLinkBio());
        temp.setIntroduction(user.getIntroduction());

        temp.setCreatedAt(user.getCreatedAt());
        temp.setUpdatedAt(user.getUpdatedAt());
        return temp;
    }
}
