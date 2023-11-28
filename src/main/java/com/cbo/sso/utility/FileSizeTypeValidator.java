package com.cbo.sso.utility;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeTypeValidator
        implements ConstraintValidator<FileSize, MultipartFile> {

    private static final Integer MB=1024*1024;

    private long maxSizeInMB;

    @Override
    public void initialize(FileSize fileSize) {
        this.maxSizeInMB=fileSize.maxSizeInMB();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile,
                           ConstraintValidatorContext
                                   constraintValidatorContext) {

        if(multipartFile==null)
            return true;


        boolean result = true;
        System.out.println("here we are isValid");
        String contentType = multipartFile.getContentType();
        if (!isSupportedContentType(contentType)) {
            result = false;
        }
        System.out.println(result);
        return (multipartFile.getSize()<maxSizeInMB*MB & result);
    }
    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}
