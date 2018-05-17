package com.hifs.hifs.service.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifs.hifs.entity.Role;
import com.hifs.hifs.entity.User;

public interface IUserService extends JpaRepository<User, Integer>  {
	

    public List<User> findByUserName(String userName);
	
}
