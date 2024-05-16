package com.example.patronus.payload.request;

import com.example.patronus.annotation.Password;
import com.example.patronus.models.jpa.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;

    @NotBlank
    @Size(min = 6, max = 40)
    @Password
    private String password;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email(message = "Please enter valid e-mail address")
    @Size(min = 7, message = "Minimum e-mail length is 7 characters.")
    private String email;

}