package org.csh.springsecurityjwt.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {
	@Id
	private String userName;
	private String password;
	private String role;
}
