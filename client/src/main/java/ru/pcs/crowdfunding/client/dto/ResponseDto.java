package ru.pcs.crowdfunding.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {

    private boolean success = true;
    private List<String> error = null;
    private Object data = null;

    public <T> T getDataAs(Class<T> cls, ObjectMapper mapper) {
        if (data == null){
            return null;
        }
        return mapper.convertValue(data, cls);
    }

    /** Не используем эти методы... Это норм? */
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