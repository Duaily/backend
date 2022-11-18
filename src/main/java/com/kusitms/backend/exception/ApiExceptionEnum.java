package com.kusitms.backend.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ApiExceptionEnum {

  // General Exception
  BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 변수를 확인해주세요."),
  UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "인증이 실패하였습니다."),
  ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "제한된 접근입니다."),
  NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "요청한 자원이 없습니다."),
  DUPLICATION_VALUE_EXCEPTION(HttpStatus.CONFLICT, "이미 존재하는 값입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버가 응답하지 않습니다."),


  // Custom Exception

  // Auth
  TOKEN_EXPIRE_TIME_OUT_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰 유효시간이 만료되었습니다."),
  TOKEN_DISCREPANCY_EXCEPTION(HttpStatus.UNAUTHORIZED, "access 토큰이 일치하지 않습니다."),
  TOKEN_INVALID_EXCEPTION(HttpStatus.FORBIDDEN, "권한 정보가 없는 토큰입니다."),
  REFRESHTOKEN_DISCREPANCY_EXCEPTION(HttpStatus.UNAUTHORIZED, "Refresh 토큰이 일치하지 않습니다."),
  PASSWORD_DISCREPANCY_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  AUTH_DUPLICATED_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
  LOGIN_FAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "로그인에 실패하셨습니다."),
  CODE_INVALID_EXCEPTION(HttpStatus.BAD_REQUEST, "전화번호 인증코드를 잘못 입력하셨습니다."),

  // post
  POST_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
  BUYER_INVALID_EXCEPTION(HttpStatus.BAD_REQUEST, "게시글 작성자는 본인의 집을 구매할 수 없습니다."),

  // deal
  SELLER_INVALID_EXCEPTION(HttpStatus.UNAUTHORIZED, "빈 집의 주인만 거래 완료 처리를 할 수 있습니다."),
  DEAL_DONE_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 거래 완료된 빈 집입니다."),
;

  private final HttpStatus httpStatus;
  private final Integer status;
  private final String message;

  ApiExceptionEnum(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.status = httpStatus.value();
    this.message = message;
  }
}
