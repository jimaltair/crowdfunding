package ru.pcs.crowdfunding.tran.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseDto {
    private boolean success = true;
    private List<String> error = null;
    private Object data = null;
}
