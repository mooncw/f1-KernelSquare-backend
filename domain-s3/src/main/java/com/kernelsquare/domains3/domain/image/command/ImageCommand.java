package com.kernelsquare.domains3.domain.image.command;

import lombok.Builder;

public class ImageCommand {
    @Builder
    public record FindAllImages(
        String createdDate
    ) {}
}
