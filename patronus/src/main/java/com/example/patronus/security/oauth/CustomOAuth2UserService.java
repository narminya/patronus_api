package com.example.patronus.security.oauth;

import com.example.patronus.enums.ERole;
import com.example.patronus.models.jpa.Role;
import com.example.patronus.models.jpa.User;
import com.example.patronus.repository.RoleRepository;
import com.example.patronus.repository.UserRepository;
import com.example.patronus.security.CustomUserDetails;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    public CustomOAuth2UserService(UserRepository repository, RoleRepository roleRepository, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }

        CustomUserDetails customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
        User user = upsertUser(customUserDetails);
        customUserDetails.setId(user.getId());
        return customUserDetails;
    }

    private User upsertUser(CustomUserDetails customUserDetails) {
        Optional<User> userOptional = repository.findByUsername(customUserDetails.getUsername());
        User user;
        if (userOptional.isEmpty()) {
            user = new User();
            user.setUsername(customUserDetails.getUsername());
            user.setName(customUserDetails.getName());
            user.setEmail(customUserDetails.getEmail());
            user.setImageUrl(customUserDetails.getAvatarUrl());
            user.setProvider(customUserDetails.getProvider());

            Role optionalRole = roleRepository
                    .findByName(ERole.valueOf("USER")).orElseThrow();
            user.getRoles().add(optionalRole);

        } else {
            user = userOptional.get();
            user.setEmail(customUserDetails.getEmail());
            user.setImageUrl(customUserDetails.getAvatarUrl());
        }
        return repository.save(user);
    }
}