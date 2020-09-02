package site.javaee.mall.ware.dao;

import site.javaee.mall.ware.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-08-04 17:11:33
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
