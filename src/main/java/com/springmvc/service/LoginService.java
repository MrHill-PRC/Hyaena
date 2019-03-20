package com.springmvc.service;

import com.springmvc.entity.CLS_VO_Result;
import com.springmvc.entity.TabUser;
import org.springframework.stereotype.Service;


public interface LoginService {
    public abstract CLS_VO_Result login(String userName, String password);
}
