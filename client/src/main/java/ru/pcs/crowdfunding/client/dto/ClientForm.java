package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.ClientImage;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientForm {
//    static final String TEMP_FOLDER = "temp_download";
//    static final Path DOWNLOAD_PATH = Paths.get(System.getProperty("user.home"), TEMP_FOLDER);

    public static final String CLIENTS_IMAGE_PATH = "./client/src/main/resources/static/clients_images/";

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String email;

    @NotBlank
    private BigDecimal sumAccount;

    private ClientImage image;

}
