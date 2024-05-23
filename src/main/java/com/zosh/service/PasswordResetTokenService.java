package com.zosh.service;

import com.zosh.model.PasswordResetToken;

public interface PasswordResetTokenService {

    PasswordResetToken findByToken(String token);

    void delete(PasswordResetToken resetToken);

}
