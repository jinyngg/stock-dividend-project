package com.zerobase.stockdividendproject.security;

import com.zerobase.stockdividendproject.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 5; // 5 hour
    private static final String KEY_ROLES = "roles";

    private final MemberService memberService;
    
    @Value("${spring.jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성(발급)
     * @param username
     * @param roles
     * @return
     */
    public String generateToken(String username, List<String> roles) {

        // 권한 정보 저장을 위한 클레임 생성
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);
        
        // 토큰 생성 시간
        var now = new Date();
        // 토큰 유효 시간
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
                .compact();

    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        // 토큰이 빈 값이면 유효하지 않음.
        if (!StringUtils.hasText(token)){
            return false;
        }

        var claims = this.parseClaims(token);

        // 토큰 만료시간에 현재 시간을 비교하여 유효한지 확인
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
            // 파싱 과정에서 만료가 된 토큰을 파싱하려고 할 때 예외 처리
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
