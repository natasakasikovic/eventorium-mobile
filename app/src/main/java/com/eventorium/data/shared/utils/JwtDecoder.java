package com.eventorium.data.shared.utils;

import android.util.Log;

import com.eventorium.BuildConfig;

import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtDecoder {
    private static final String SECRET = BuildConfig.SECRET;

    public static String decodeRole(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            List<String> roles = claims.get("roles", List.class);
            return roles != null && !roles.isEmpty() ? roles.get(0) : null;
        } catch (Exception e) {
            Log.e("Decode token error: ", "Cannot get role");
        }
        return "GUEST";
    }
}
