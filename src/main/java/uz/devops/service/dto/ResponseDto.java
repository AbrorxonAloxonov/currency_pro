package uz.devops.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto<T> {

    private Boolean success;
    private String message;
    private T data;

    public ResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseDto(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
