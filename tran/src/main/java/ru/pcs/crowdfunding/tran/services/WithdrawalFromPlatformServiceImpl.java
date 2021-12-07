package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawalFromPlatformServiceImpl implements WithdrawalFromPlatformService {

    @Override
    public Boolean createWithdrowalFromPlatform() {
        return true;
    }
}