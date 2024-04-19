package com.kernelsquare.domains3.domain.image.repository;

import com.kernelsquare.domains3.domain.image.command.ImageCommand;
import com.kernelsquare.domains3.domain.image.info.ImageInfo;

public interface ImageReader {
    ImageInfo findAll(ImageCommand.FindAllImages command);
}
