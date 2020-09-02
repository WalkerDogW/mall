package site.javaee.mall.member.dao;

import site.javaee.mall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 * 
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-08-04 16:52:20
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {
	
}
