package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

//@Component
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao mDao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public Member loginMember(Member m) {
		return mDao.loginMember(sqlSession, m);
	}
	
	@Override
	public String loginMember(String userId) {
		return mDao.loginMember(sqlSession, userId);
	}

	@Override
	public int insertMember(Member m) {
		return mDao.insertMember(sqlSession, m);
	}

	@Override
	public int updateMember(Member m) {
		int result = mDao.updateMember(sqlSession, m);
		return result;
	}

	@Override
	public int deleteMember(String userId) {
		return mDao.deleteMember(sqlSession, userId);
	}

	@Override
	public int idCheck(String checkId) {
		return mDao.idCheck(sqlSession, checkId);
	}
	
	

}
