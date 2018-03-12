package com.apass.zufang.service.apartment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.apass.gfb.framework.exception.BusinessException;
/**
 * ImageService
 * @author Administrator
 *
 */
@Service
public class ImageService {

    @Value("${zufang.image.uri}")
    private String path;
    /**
     * @param imageUrl 加密前的图片路径
     * @return String
     */
    public String getComplateUrl(String imageUrl) throws BusinessException {
        if (StringUtils.isBlank(path)) {
            throw new BusinessException("PATH为空");
        }
        if (StringUtils.isBlank(imageUrl)) {
            return null;
        } else {
            return path + imageUrl;
        }
    }
}