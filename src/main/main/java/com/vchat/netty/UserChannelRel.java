package com.vchat.netty;/**
 * Created by Administrator on 2019/7/25.
 */

import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName UserChannelRel
 * @Description TODO
 * @Date 2019/7/25 15:15
 **/
public class UserChannelRel {
    private static Map<String,Channel> userIdChannelManager = new ConcurrentHashMap<>();

    public static void put(String userId,Channel channel){
        userIdChannelManager.put(userId,channel);
    }

    public static Channel get(String userId){
        return userIdChannelManager.get(userId);
    }

    public static void output() {
        for (Map.Entry<String, Channel> entry : userIdChannelManager.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}
