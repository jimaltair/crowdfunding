package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.pcs.crowdfunding.client.dto.ImageDto;
import ru.pcs.crowdfunding.client.services.ImageService;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/project_images/{id}")
    public ResponseEntity<byte[]> getProjectImage(@PathVariable("id") Long id, HttpServletResponse response) {
        log.info("get project image by id {}", id);

        Optional<ImageDto> imageDto = imageService.getProjectImageById(id);
        if (!imageDto.isPresent()) {
            log.error("project image with id {} not found", id);
            return ResponseEntity.notFound().build();
        }

        log.info("found project image with id {} and content length {}", id, imageDto.get().getContent().length);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/" + FilenameUtils.getExtension(imageDto.get().getFileName())));
        headers.setContentDispositionFormData("attachment", imageDto.get().getFileName());
        headers.setContentLength(imageDto.get().getContent().length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageDto.get().getContent());
    }
}
