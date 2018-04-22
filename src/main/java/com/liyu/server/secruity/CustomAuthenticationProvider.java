package com.liyu.server.secruity;

import com.liyu.server.service.AccountService;
import com.liyu.server.service.impl.AccountServiceImpl;
import com.liyu.server.tables.pojos.Account;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 认证逻辑
        Account account = accountService.getByUsernameAndPassword(name, password);
        if (account != null) {
            // 这里设置权限和角色
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
            authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));
            // 生成令牌
            return new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword(), authorities);
        } else {
            throw new BadCredentialsException("Unauthorized");
        }
    }

    // 是否可以提供输入类型的认证服务
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}