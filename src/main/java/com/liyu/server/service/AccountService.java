package com.liyu.server.service;

import com.liyu.server.tables.pojos.Account;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AccountService {

    /**
     * @return
     */
    List<Account> List();
}
