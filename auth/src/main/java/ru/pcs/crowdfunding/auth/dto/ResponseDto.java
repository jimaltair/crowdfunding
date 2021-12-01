package ru.pcs.crowdfunding.auth.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {
    private boolean success = true;
    private List<String> error = null;
    private Object data = null;

    public static ResponseDto buildResponse(boolean success, HttpStatus status, String errorMessage, Object data){
        return ResponseDto.builder()
                .success(success)
                .error(success ? null : Arrays.asList(status.toString(), errorMessage))
                .data(data)
                .build();
    }
}
