package tn.iset.gestion_formation.security.api;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    public record LoginRequest(String username, String password) {}
    public record TokenResponse(String accessToken, String tokenType, long expiresIn) {}

    private final AuthenticationManager authManager;
    private final JwtEncoder jwtEncoder;
    private final long expiration;

    public AuthRestController(AuthenticationManager apiAuthenticationManager,
                              JwtEncoder jwtEncoder,
                              @Value("${app.jwt.expiration-seconds}") long expiration) {
        this.authManager = apiAuthenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.expiration = expiration;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

            Instant now = Instant.now();
            List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("gestion-formation")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiration))
                .subject(auth.getName())
                .claim("roles", roles)
                .build();

            String token = jwtEncoder.encode(
                JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();

            return ResponseEntity.ok(new TokenResponse(token, "Bearer", expiration));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Identifiants invalides");
        }
    }
}