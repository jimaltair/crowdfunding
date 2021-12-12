package ru.pcs.crowdfunding.auth.dto;

import lombok.*;

import java.util.Arrays;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private boolean success = true;
    private List<String> error = null;
    private Object data = null;

    public static ResponseDto buildResponse(boolean success, String errorMessage, Object data){
        return ResponseDto.builder()
                .success(success)
                .error(success ? null : Arrays.asList(errorMessage))
                .data(data)
                .build();
    }

}