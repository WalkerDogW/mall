package site.javaee.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-08-04 17:11:33
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

