package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.dto.OperationDto;
import ru.pcs.crowdfunding.client.security.JwtTokenProvider;
import ru.pcs.crowdfunding.client.services.OperationService;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


@Controller
@RequestMapping("/operation")
@RequiredArgsConstructor
@Slf4j
public class OperationController {

    private final OperationService operationService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/top_up")
    public String createTopUpOperation(HttpServletRequest request,
                                       @RequestParam("sum") BigDecimal sumTopUp,
                                       @RequestParam("client_id") Long clientId,
                                       @RequestParam("account_id") Long accountId) {

        log.info("post /api/operation/top_up: post operation TOP_UP with " +
                "clientId = {}, accountId = {}, sum = {}", clientId, accountId, sumTopUp);

        try {
            String token = getTokenFromCookie(request);
            Long tokenClientId = tokenProvider.getClientIdFromToken(token);
            if(tokenClientId != clientId) {
                throw new IllegalArgumentException("Not enough rights for this operation");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "redirect:/clients/" + clientId + "?error=" + e;
        }

        OperationDto operationDto = OperationDto.builder()
               .operationType(OperationDto.Type.TOP_UP)
               .sum(sumTopUp)
               .initiatorId(clientId)
               .debitAccountId(accountId)
               .build();

        try {
            operationDto = operationService.operate(operationDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/clients/" + clientId + "?error=" + e;
        }
        return "redirect:/clients/" + clientId;
    }

    private String getTokenFromCookie(HttpServletRequest request) throws IllegalAccessException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SignUpController.TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("Not enough rights for this operation");
    }

}