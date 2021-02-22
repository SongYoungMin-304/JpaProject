package com.example.demo.form;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class MemberForm {
	
	@NotEmpty(message ="ȸ�� �̸��� �ʼ� �Դϴ�.")
	private String name;
	
	private String city;
	private String street;
	private String zipcode;
}