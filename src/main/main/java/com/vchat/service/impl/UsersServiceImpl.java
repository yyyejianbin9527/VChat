package com.vchat.service.impl;/**
 * Created by Administrator on 2019/7/24.
 */

import com.vchat.mapper.*;
import com.vchat.pojo.ChatMsg;
import com.vchat.pojo.Users;
import com.vchat.pojo.vo.FriendRequestVO;
import com.vchat.pojo.vo.MyFriendsVO;
import com.vchat.service.IUsersService;
import com.vchat.utils.FastDFSClient;
import com.vchat.utils.FileUtils;
import com.vchat.utils.QRCodeUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName UsersServiceImpl
 * @Description TODO
 * @Date 2019/7/24 14:33
 **/
@Service
public class UsersServiceImpl implements IUsersService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersMapperCustom usersMapperCustom;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        Users user = new Users();
        user.setUsername(username);

        Users result = usersMapper.selectOne(user);

        return result != null ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String pwd) {

        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", pwd);

        Users result = usersMapper.selectOneByExample(userExample);

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users user) {

        String userId = sid.nextShort();

        // 为每个用户生成一个唯一的二维码
        String qrCodePath = "C://user" + userId + "qrcode.png";
        // muxin_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath, "muxin_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);

        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setQrcode(qrCodeUrl);

        user.setId(userId);
        usersMapper.insert(user);

        return user;
    }

    @Override
    public Users updateUserInfo(Users user) {
        return null;
    }

    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        return null;
    }

    @Override
    public Users queryUserInfoByUsername(String username) {
        return null;
    }

    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {

    }

    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return null;
    }

    @Override
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {

    }

    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {

    }

    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        return null;
    }

    @Override
    public String saveMsg(ChatMsg chatMsg) {
        return null;
    }

    @Override
    public void updateMsgSigned(List<String> msgIdList) {

    }

    @Override
    public List<com.vchat.pojo.ChatMsg> getUnReadMsgList(String acceptUserId) {
        return null;
    }
}
