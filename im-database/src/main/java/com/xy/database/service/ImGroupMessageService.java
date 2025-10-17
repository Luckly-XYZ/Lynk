package com.xy.database.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.domain.po.ImGroupMessagePo;

import java.util.List;


/**
 * @author dense
 * @description 针对表【im_group_message】的数据库操作Service
 */
public interface ImGroupMessageService extends IService<ImGroupMessagePo> {

    List<ImGroupMessagePo> list(String userId, Long sequence);

    ImGroupMessagePo last(String userId, String groupId);

    Integer selectReadStatus(String groupId, String toId, Integer code);
}
