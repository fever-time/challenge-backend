package shop.fevertime.backend.dto.reponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtResponse {
    private final String token;
    private final String username;
}