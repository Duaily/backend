package com.kusitms.backend.config;

import com.kusitms.backend.domain.User;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.RedisClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final CustomUserDetailService customUserDetailsService;
  private final UserRepository userRepository;
  private final RedisClient redisClient;
  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "bearer";

  @Value("${spring.jwt.access-token}")
  private long accessTokenExpireTime;
  @Value("${spring.jwt.refresh-token}")
  private long refreshTokenExpireTime;

  @Value("${spring.jwt.secret}")
  private String secret;

  @PostConstruct
  protected void init() {
    secret = Base64.getEncoder().encodeToString(secret.getBytes());
  }

  public String createAccessToken(String email) {
    return generateTokenDto(email, accessTokenExpireTime);
  }

  public String createRefreshToken(User user) {
    String refreshToken = generateTokenDto(user.getEmail(), refreshTokenExpireTime);
    //user.updateRefreshToken(refreshToken);
    return refreshToken;
  }

  public String generateTokenDto(String email, Long validTime) {
    Claims claims = Jwts.claims().setSubject(email);

    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + validTime))
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  public Authentication getAuthentication(String token) {

    UserDetails principle = customUserDetailsService.loadUserByUsername(getUserEmail(token));

    return new UsernamePasswordAuthenticationToken(principle, "", principle.getAuthorities());
  }

  private String getUserEmail(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader("X-AUTH-TOKEN");
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claim = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
      return !claim.getBody().getExpiration().before(new Date());
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }

}
