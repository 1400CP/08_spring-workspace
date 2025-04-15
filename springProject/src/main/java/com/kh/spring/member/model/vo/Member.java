package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * * Lombok(롬복)
 * 1. 라이브러리 다운로드 후 적용(Maven pom.xml)
 * 2. 다운로드된 jar 찾아서 설치 (작업할 IDE 선택해서 설치)
 * 3. IDE 재실행
 * 주의 : 필드명 작성 시, 적어도 소문자 2글자 이상으로 만들 것.
 */

@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 전체매개변수생성자
@Setter
@Getter
@ToString

public class Member {
	
	private String userId;
	private String userPwd;
	private String userName;
	private String email;
	private String gender;
//	private int age;
	private String age;
	private String phone;
	private String address;
	private Date enrollDate;
	private Date modifyDate;
	private String status;
	
	
}
