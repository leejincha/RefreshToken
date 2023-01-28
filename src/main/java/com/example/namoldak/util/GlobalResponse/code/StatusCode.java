package com.example.namoldak.util.GlobalResponse.code;


import lombok.Getter;
import org.springframework.http.HttpStatus;

// 기능 : 응답용 메세지 커스텀
@Getter
public enum StatusCode {

    //TODO ========================= 예외 응답 코드 ===============================

    // 400 BAD_REQUEST : 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "요청이 올바르지 않습니다"),
    BAD_REQUEST_TOKEN(HttpStatus.BAD_REQUEST, "400","토큰이 유효하지 않습니다."),
    BAD_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "400","refreshtoken 삭제 권한이 없습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "400","중복된 이메일이 존재합니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "400","중복된 닉네임이 존재합니다."),
    NOTEXIST_EMAIL(HttpStatus.BAD_REQUEST, "400","존재하지 않는 이메일입니다."),
    LOGIN_MATCH_FAIL(HttpStatus.BAD_REQUEST, "400","회원을 찾을 수 없습니다."),

    INVALID_ID_PASSWORD(HttpStatus.BAD_REQUEST, "400","아이디나 비밀번호의 구성이 알맞지 않습니다"),
    BAD_PASSWORD(HttpStatus.BAD_REQUEST, "400","비밀번호가 일치하지 않습니다"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "403","로그인 후 사용이 가능합니다"),
    NO_AUTH_MEMBER(HttpStatus.BAD_REQUEST, "403","작성자 정보와 일치하지 않습니다."),
    LOGIN_MEMBER_ID_FAIL(HttpStatus.NOT_FOUND, "110", "존재하지 않는 유저 정보입니다."),
    LOGIN_PASSWORD_FAIL(HttpStatus.BAD_REQUEST, "111", "비밀번호가 일치하지 않습니다."),
    LOGIN_WRONG_SIGNATURE_JWT_TOKEN(HttpStatus.BAD_REQUEST, "112", "잘못된 JWT 서명입니다."),
    LOGIN_EXPIRED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "113", "만료된 JWT 토큰입니다."),
    LOGIN_NOT_SUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "114", "지원되지 않는 JWT 토큰입니다."),
    LOGIN_WRONG_FORM_JWT_TOKEN(HttpStatus.BAD_REQUEST, "115", "JWT 토큰이 잘못되었습니다."),
    LOGIN_MEMBER_REQUIRED_INFORMATION_FAIL(HttpStatus.BAD_REQUEST, "116", "필수 입력 정보를 입력 후 시도해주세요"),
    NOT_MATCH_POST(HttpStatus.BAD_REQUEST, "117", "현재 로그인한 유저가 작성한 게시글이 아닙니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST,"117","이미 존재하는 닉네임입니다."),
    DUPLICATED_PASSWORD(HttpStatus.BAD_REQUEST,"118","비밀번호가 틀립니다."),
    POST_ERROR(HttpStatus.BAD_REQUEST,"119","게시글 작성이 필요합니다."),
    NOT_EXIST_MEDIA(HttpStatus.BAD_REQUEST,"120","이미지가 존재하지 않아 게시글 작성이 불가합니다."),
    NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST,"121","입장할 방이 존재하지 않습니다."),
    CANT_ENTER(HttpStatus.BAD_REQUEST,"122","정원이 다 차있닭!!"),
    MEMBER_DUPLICATED(HttpStatus.BAD_REQUEST,"123","이미 입장해있닭!!"),
    UNAUTHORIZE(HttpStatus.UNAUTHORIZED,"124","방장만이 게임 시작을 진행할 수 있습니다."),
    ALREADY_PLAYING(HttpStatus.BAD_REQUEST,"125","게임이 시작해서 못 들어간닭!!"),
    NOT_MATCH_PLAYER(HttpStatus.BAD_REQUEST,"126","해당 방의 참가자가 아니라 준비를 진행할 수 없습니다."),
    NOT_READY(HttpStatus.BAD_REQUEST,"127","모든 인원이 준비완료 상태가 아니라 게임을 시작할 수 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,"128","이미 존재하는 이메일입니다."),
    ROOMNAME_OVER(HttpStatus.BAD_REQUEST,"129","생성할 수 있는 방 이름 글자 수가 초과하였습니다."),
    NOT_ENOUGH_MEMBER(HttpStatus.BAD_REQUEST,"130","게임 시작하기에 충분한 유저가 모이지 않았습니다."),
    ROOMNAME_BLANK(HttpStatus.BAD_REQUEST,"131","게임방 이름은 공백일 수 없습니다."),
    NOT_EXIST_ROOMS(HttpStatus.BAD_REQUEST,"132","조건에 맞는 방이 존재하지 않습니다."),
    SPOTLIGHT_ERR(HttpStatus.BAD_REQUEST,"133","스포트라이트 처리에서 예외가 발생했습니다."),
    SIGNATURE_EXCEPTION(HttpStatus.BAD_REQUEST,"134","JWT 서명에 문제가 발생했습니다."),

    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"404","댓글이 존재하지 않습니다."),
    SEARCH_POST_ERROR(HttpStatus.BAD_REQUEST,"141","검색 결과에 맞는 게시글이 존재하지 않습니다."),
    ACCESS_DENIED(HttpStatus.NOT_ACCEPTABLE, "403", "접근이 불가능합니다."),
    INTERNAL_SERVER_ERROR_PLZ_CHECK(HttpStatus.INTERNAL_SERVER_ERROR, "999", "알수없는 서버 내부 에러 발생 , dladlsgur3334@gmail.com 으로 연락 부탁드립니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 게시글이 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 챗룸 정보가 없습니다."),
    IN_CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 챗룸에 해당 유저 정보가 없습니다."),
    GAME_SET_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "게임 스타트 셋을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.NOT_FOUND, "404", "파일 업로드 실패"),
    FILE_DELETE_FAILED(HttpStatus.NOT_FOUND, "404", "파일 삭제 실패"),


    //TODO ========================= 성공 응답 코드 ===============================

    OK(HttpStatus.OK, "200", "응답이 정상 처리 되었습니다."),
    LOGIN_OK(HttpStatus.OK, "200", "로그인 되셨습니다!"),
    SIGNOUT_OK(HttpStatus.OK, "200", "로그아웃 되셨습니다!"),
    SIGNUP_OK(HttpStatus.OK, "200","회원가입에 성공했습니다."),
    GET_OK(HttpStatus.OK,"200", "조회 성공했습니다."),
    CREATE_OK(HttpStatus.OK,"200", "생성 성공했습니다."),
    CREATE_ROOM(HttpStatus.OK,"200", "게임방을 생성했습니닭!"),
    MODIFY_OK(HttpStatus.OK,"200", "수정 성공했습니다."),
    DELETE_OK(HttpStatus.OK,"200", "삭제 성공했습니다."),
    LIKE_CHECK(HttpStatus.OK,"200", "좋아요 성공했습니다."),
    AVAILABLE_EMAIL(HttpStatus.OK,"200", "사용 가능한 이메일 입니다."),
    AVAILABLE_NICKNAME(HttpStatus.OK,"200", "사용 가능한 닉네임 입니다."),
    SEND_EMAIL(HttpStatus.OK,"200", "인증 메일이 발송되었습니다."),
    REGISTER_OK(HttpStatus.OK,"200", "가입 완료 되었습니다."),
    EXIT_SUCCESS(HttpStatus.OK,"200", "방을 나가셨습니닭!"),
    ENTER_OK(HttpStatus.OK,"200", "방에 입장하셨습니닭!"),
    DELETE_MEMBER_OK(HttpStatus.OK,"200", "회원 탈퇴 성공했습니다."),
    GAME_START(HttpStatus.OK,"200", "게임 시작!");


    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String statusMsg;

    StatusCode(HttpStatus httpStatus, String statusCode, String statusMsg) {
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
}
