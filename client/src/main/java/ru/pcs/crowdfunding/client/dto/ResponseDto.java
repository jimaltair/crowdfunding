package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {
    private boolean success;
    private List<String> error;
    private Object data;

    public static ResponseDto buildSuccess(Object data) {
        return ResponseDto.builder()
                .success(true)
                .error(null)
                .data(data)
                .build();
    }

    public static ResponseDto buildError(String ... error) {
        return ResponseDto.builder()
                .success(false)
                .error(Arrays.asList(error))
                .data(null)
                .build();
    }
}
