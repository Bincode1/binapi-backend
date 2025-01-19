package com.bin.binapibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bin.bincommon.model.UserInterfaceInfo;

import java.util.List;

/**
* @author 26961
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2024-12-26 13:22:27
* @Entity com.bin.binapibackend.model.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    //select interfaceInfoId, sum(totalNum) as totalNum
    //          from user_interface_info
    //                group by interfaceInfoId
    //                      order by totalNum desc limit 3
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




