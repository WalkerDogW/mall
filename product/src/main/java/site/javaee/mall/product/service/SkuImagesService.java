package site.javaee.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-07-31 11:00:50
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

