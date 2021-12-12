package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.OperationDto;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.security.JwtTokenProvider;
import ru.pcs.crowdfunding.client.services.ClientsService;
import ru.pcs.crowdfunding.client.services.OperationService;
import ru.pcs.crowdfunding.client.services.ProjectsService;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;


@Controller
@RequestMapping("/operation")
@RequiredArgsConstructor
@Slf4j
public class OperationController {

    private final OperationService operationService;
    private final ClientsService clientsService;
    private final ProjectsService projectsService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/top_up")
    public String createTopUpOperation(Authentication authentication,  HttpServletRequest request,
                                       @RequestParam("sum") BigDecimal sumTopUp,
                                       @RequestParam("client_id") Long clientId) {

        log.info("post /api/operation/top_up: post operation TOP_UP with " +
                "clientId = {}, sum = {}", clientId, sumTopUp);

        Long accountId;

        try {
            String token = getTokenFromCookie(request);
            Long tokenClientId = tokenProvider.getClientIdFromToken(token);

            if(tokenClientId != clientId ) {
                throw new IllegalArgumentException("Not enough rights for this operation");
            }

            accountId = clientsService.getAccountIdByClientId(tokenClientId);

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
            log.info("Getting operationDto from tran service = {} ", operationDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/clients/" + clientId + "?error=" + e;
        }
        return "redirect:/clients/" + clientId;
    }

    @PostMapping("/payment")
    public String createPaymentOperation(Authentication authentication,  HttpServletRequest request,
                                       @RequestParam("sumPayment") BigDecimal sumPayment,
                                       @RequestParam("project_id") Long projectId) {

        log.info("post /api/operation/payment: post operation PAYMENT with " +
                "project_id = {}, sum = {}", projectId, sumPayment);

        Long tokenClientId;
        Long clientAccountId;
        Long projectAccountId;

        try {
            String token = getTokenFromCookie(request);
            tokenClientId = tokenProvider.getClientIdFromToken(token);
            log.info("Found tokenClientId = {} ", tokenClientId);

            clientAccountId = clientsService.getAccountIdByClientId(tokenClientId);
            projectAccountId = projectsService.getAccountIdByProjectId(projectId);

        } catch (IllegalAccessException e) {
                e.printStackTrace();
                return "redirect:/projects/" + projectId + "?error=" + e;
            }

        OperationDto operationDto = OperationDto.builder()
                .operationType(OperationDto.Type.PAYMENT)
                .sum(sumPayment)
                .initiatorId(tokenClientId)
                .debitAccountId(projectAccountId)
                .creditAccountId(clientAccountId)
                .build();

        try {
            operationDto = operationService.operate(operationDto);
            log.info("Getting operationDto from tran service = {} ", operationDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/projects/" + projectId + "?error=" + e;
        }
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/withdrow")
    public String createWithdrawOperation(HttpServletRequest request,
                                         @RequestParam("project_id") Long projectId) {

        log.info("post /api/operation/withdraw: post operation WITHDRAW with project_id = {}", projectId);

        Long tokenClientId;
        try {
            String token = getTokenFromCookie(request);
            tokenClientId = tokenProvider.getClientIdFromToken(token);
            log.info("Found tokenClientId = {} ", tokenClientId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "redirect:/projects/" + projectId + "?error=" + e;
        }

        try {
            operationService.withdrawMoneyFromProject(projectId, tokenClientId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/projects/" + projectId + "?error=" + e;
        }
        return "redirect:/projects/" + projectId;
    }


    //Временный метод до запуска Spring Security и получения id пользователя из контекста
    private String getTokenFromCookie(HttpServletRequest request) throws IllegalAccessException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SignUpController.TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        throw new IllegalAccessException("Not enough rights for this operation");
    }

}