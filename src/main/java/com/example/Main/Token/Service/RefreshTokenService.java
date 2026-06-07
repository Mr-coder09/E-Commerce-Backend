package com.example.Main.Token.Service;


import com.example.Main.Token.Entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    RefreshToken validateRefreshToken(String token);

    void revokeRefreshToken(String token);

   
    void revokeAllForUser(Long userId);
}