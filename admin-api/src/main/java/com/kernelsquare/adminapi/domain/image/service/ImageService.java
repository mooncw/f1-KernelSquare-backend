package com.kernelsquare.adminapi.domain.image.service;

import com.kernelsquare.adminapi.domain.image.dto.UploadImageResponse;
import com.kernelsquare.adminapi.domain.image.validation.ImageValidation;
import com.kernelsquare.domains3.domain.image.command.ImageCommand;
import com.kernelsquare.domains3.domain.image.info.ImageInfo;
import com.kernelsquare.domains3.domain.image.repository.ImageReader;
import com.kernelsquare.domains3.domain.image.repository.ImageStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageStore imageStore;
	private final ImageReader imageReader;

	public UploadImageResponse uploadImage(String category, MultipartFile multipartFile) {
		ImageValidation.validateCategory(category);

		ImageValidation.validateFileExists(multipartFile);

		ImageValidation.validateFileExtension(multipartFile);

		String imageUrl = imageStore.store(category, multipartFile);

		return UploadImageResponse.from(imageUrl);
	}

	public void deleteImage(String imageUrl) {
		imageStore.delete(imageUrl);
	}

	public ImageInfo findAllImages(ImageCommand.FindAllImages command) {
		return imageReader.findAll(command);
	}
}
