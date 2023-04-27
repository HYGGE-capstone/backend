package hygge.backend.jwt;

public interface JwtProperties {
    int EXPIRATION_TIME = (1000 * 60) * 10; // 10m
    int REFRESH_EXPIRATION_TIME = (1000 * 60 * 60 * 24) * 14; // 14 day
    String ACCESS_TOKEN = "Access Token";
    String REFRESH_TOKEN = "Refresh Token";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_PREFIX = "Authorization";
    String REFRESH_HEADER_PREFIX = "Authorization-refresh";
    String ID = "id";
    String LOGIN_ID = "loginId";

}
