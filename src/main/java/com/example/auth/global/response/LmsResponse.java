package com.example.auth.global.response;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LmsResponse<T> {
    private HttpStatus code;
    private T data;
    private String msg;
    private String errorMsg;
    private LocalDateTime currentTime;

    public LmsResponse(HttpStatus code, T data, String msg, String errorMsg, LocalDateTime currentTime) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.errorMsg= errorMsg;
        this.currentTime = currentTime;
    }
}
