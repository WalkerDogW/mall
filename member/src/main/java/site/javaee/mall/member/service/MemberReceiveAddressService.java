package site.javaee.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.member.entity.MemberReceiveAddressEntity;

import java.util.Map;

/**
 * 会员收货地址
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-08-04 16:52:20
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

