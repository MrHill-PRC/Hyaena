package com.springmvc.service.imp;

import com.springmvc.dao.TabUserMapper;
import com.springmvc.entity.CLS_VO_Result;
import com.springmvc.entity.TabUser;
import com.springmvc.service.LoginService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoginServiceImp implements LoginService {
    @Resource
    TabUserMapper tabUserMapper;

    /**
     * 登录验证
     * @param userName
     * @param password
     */
    public CLS_VO_Result login(String userName, String password) {
        CLS_VO_Result result = new CLS_VO_Result();
        TabUser tabUser = new TabUser();
        tabUser.setsName(userName);
        tabUser.setsPassword(password);
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            result.setRet(-7);
        }
        List<TabUser> userList = tabUserMapper.selctByPrimary(tabUser);
        System.out.println("查询出来的用户数量："+userList.size());
        if (userList.size() > 0){
            if (userList.get(0) !=null) {
                tabUser = userList.get(0);
            }
            result.setContent(tabUser);
            result.setRet(0);
        } else {
            result.setRet(-7);
        }
        return result;
    }
}
