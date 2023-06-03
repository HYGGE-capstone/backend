package hygge.backend.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionInfo {
    CANNOT_FIND_MEMBER("CF-000", "요청하신 멤버를 찾을 수 없습니다."),
    CANNOT_FIND_TEAM("CF-001", "요청하신 팀을 찾을 수 없습니다."),
    CANNOT_FIND_SUBJECT("CF-002", "요청하신 과목을 찾을 수 없습니다."),
    CANNOT_FIND_MEMBER_TEAM("CF-003", "요청하신 소속 팀을 찾을 수 없습니다."),
    CANNOT_FIND_OFFER("CF-004", "요청하신 팀 합류 제안을 찾을 수 없습니다."),
    CANNOT_FIND_RESUME("CF-005", "요청하신 이력서를 찾을 수 없습니다."),
    CANNOT_FIND_SUBSCRIBE("CF-006", "요청하신 구독 정보를 찾을 수 없습니다."),
    CANNOT_FIND_TEAM_APPLICANT("CF-007", "요청하신 팀 지원 정보를 찾을 수 없습니다."),
    CANNOT_FIND_MESSAGE_ROOM("CF-008", "요청하신 쪽지함을 찾을 수 없습니다."),
    CANNOT_FIND_SCHOOL("CF-009", "요청하신 학교를 찾을 수 없습니다."),
    CANNOT_FIND_NOTICE("CF-010", "요청하신 공지를 찾을 수 없습니다."),

    // 로그인/로그아웃
    INVALID_REFRESH_TOKEN("LG-000", "유효하지 않은 리프레쉬 토큰입니다."),
    INVALID_ACCESS_TOKEN("LG-001", "유효하지 않은 액세스 토큰입니다."),
    LOGOUT_MEMBER("LG-002", "로그아웃 된 회원입니다."),
    REFRESH_TOKEN_MATCH_FAIL("LG-003", "리프레쉬 토큰과 유저 정보가 일치하지 않습니다."),

    // 회원
    REGISTERED_LOGIN_ID("MB-000", "이미 등록된 로그인 아이디입니다."),
    UNAUTHORIZED_REQUEST("MB-001", "요청할 권한이 없는 회원입니다."),

    // 이메일
    INVALID_EMAIL_FORM("EM-000", "올바르지 않은 이메일 형식입니다."),
    UNREGISTERED_EMAIL_FORM("EM-001", "등록되지 않은 학교의 이메일 형식입니다."),
    REGISTERED_EMAIL("EM-002", "이미 등록된 이메일입니다."),

    // 학교
    UNREGISTERED_SCHOOL("SC-000", "등록되지 않은 학교입니다."),

    // 팀
    DUPLICATED_OFFER("TM-000", "이미 팀 제안을 하였습니다."),
    ALREADY_HAVE_TEAM("TM-001", "요청하신 과목에 이미 소속된 팀이 있습니다."),
    MAX_TEAM_MEMBER("TM-002", "더 이상 팀원을 받을 수 없습니다.(최대인원)"),
    ALREADY_APPLY("TM-003", "이미 해당 팀에 지원하였습니다."),
    MIN_TEAM_MEMBER("TM-004", "최대 멤버수는 2 이상이어야 합니다."),

    // 이력서
    ALREADY_HAVE_RESUME("RS-000", "이력서가 이미 존재합니다."),

    // 구독
    ALREADY_SUBSCRIBE("SB-000", "이미 구독한 과목입니다."),

    // 과목
    TEAMS_EXIST("SJ-000", "과목에 소속된 팀이 존재합니다."),

    // 공지
    ALREADY_HAVE_NOTICE("NT-000", "공지가 이미 존재합니다."),
    NOT_HAVE_NOTICE("NT-001", "등록된 공지가 없습니다.");

    private final String code;
    private final String msg;
}
