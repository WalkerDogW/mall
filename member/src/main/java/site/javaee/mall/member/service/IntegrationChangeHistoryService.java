package site.javaee.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-08-04 16:52:20
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

