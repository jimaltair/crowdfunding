package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositingOnPlatformServiceImpl implements DepositingOnPlatformService {

    @Override
    public Boolean createDepositOnPlatform() {
        return true;
    }

}