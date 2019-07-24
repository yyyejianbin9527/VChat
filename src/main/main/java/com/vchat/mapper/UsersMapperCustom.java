package com.vchat.mapper;

import java.util.List;

import com.vchat.pojo.Users;
import com.vchat.pojo.vo.FriendRequestVO;
import com.vchat.pojo.vo.MyFriendsVO;
import com.vchat.utils.MyMapper;

public interface UsersMapperCustom extends MyMapper<Users> {
	
	public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
	
	public List<MyFriendsVO> queryMyFriends(String userId);
	
	public void batchUpdateMsgSigned(List<String> msgIdList);
	
}