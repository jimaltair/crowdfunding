package ru.pcs.crowdfunding.tran.dto;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private boolean success = true;
    private List<String> error = null;
    private Object data = null;

    public static ResponseDto buildSuccess(Object data) {
        return ResponseDto.builder()
                .success(true)
                .error(null)
                .data(data)
                .build();
    }

    public static ResponseDto buildError(String... error) {
        return ResponseDto.builder()
                .success(false)
                .error(Arrays.asList(error))
                .data(null)
                .build();
    }

}