package com.xy.connect.message.process.impl;


import com.xy.connect.channel.UserChannelMap;
import com.xy.connect.config.LogConstant;
import com.xy.connect.message.process.MessageProcess;
import com.xy.core.enums.IMessageType;
import com.xy.core.model.IMessageWrap;
import com.xy.spring.annotations.core.Autowired;
import com.xy.spring.annotations.core.Service;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * 群聊消息处理
 */
@Slf4j(topic = LogConstant.Message)
@Service("groupMessageProcess")
public class GroupMessageProcess implements MessageProcess {

    @Autowired
    private UserChannelMap userChannelMap;

    @Override
    public void dispose(IMessageWrap<Object> messageWrap) {

        log.info("接收到消息，接收者:{}，内容:{}", messageWrap.getIds(),
                messageWrap.getData());

        try {

            List<String> ids = messageWrap.getIds();

            // 2. 遍历当前netty中存在的指定群聊用户
            for (String id : ids) {
                // 3. 获取群聊接收者的channel
                Collection<Channel> ctxMap = userChannelMap.getChannelsByUser(id);

                for (Channel ctx : ctxMap) {

                    // 4. 推送消息到接收者
                    if (ctx != null && ctx.isOpen()) {

                        // 推送消息到用户
                        ctx.writeAndFlush(messageWrap);

                    } else {
                        // 消息推送失败确认
                        log.error("未找到WS连接，接收者:{}，内容:{}", messageWrap.getIds(),
                                messageWrap.getData());
                    }

                }

            }
        } catch (Exception e) {
            log.error("发送异常，接收者:{}，内容:{}", messageWrap.getIds(),
                    messageWrap.getData());
        }
    }


}
