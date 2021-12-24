package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.security.CrowdfundingUtils;
import ru.pcs.crowdfunding.client.services.OperationService;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;


@Controller
@RequestMapping("/operation")
@RequiredArgsConstructor
@Slf4j
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/top_up")
    public String createTopUpOperation(@RequestParam("sum") BigDecimal sumTopUp) {

        log.info("post /api/operation/top_up: post operation TOP_UP with " +
                "sum = {}", sumTopUp);

        Optional<Long> clientId = CrowdfundingUtils.getClientIdFromRequestContext();
        if (!clientId.isPresent()) {
            log.warn("Can't get user id from RequestContext");
            return "redirect:/clients/";
        }
        log.info("Get clientId = {} from security context", clientId.get());
        Long contextClientId = clientId.get();

        try {
            operationService.topUpToClient(contextClientId, sumTopUp);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/clients/" + "?error=" + e;
        }
        return "redirect:/clients/";

    }

    @PostMapping("/payment")
    public String createPaymentOperation(@RequestParam("sumPayment") BigDecimal sumPayment,
                                       @RequestParam("project_id") Long projectId) {

        log.info("post /api/operation/payment: post operation PAYMENT with " +
                "project_id = {}, sum = {}", projectId, sumPayment);

        Optional<Long> clientId = CrowdfundingUtils.getClientIdFromRequestContext();
        if (!clientId.isPresent()) {
            log.warn("Can't get user id from RequestContext");
            return "redirect:/clients/";
        }
        log.info("Get clientId = {} from security context", clientId.get());
        Long contextClientId = clientId.get();

        try {
            log.info("Start operation PAYMENT with projectId = {}, contextClientId = {}, sumPayment = {}",
                    projectId, contextClientId, sumPayment);
            operationService.paymentToProject(projectId, contextClientId, sumPayment);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/projects/" + projectId + "?error=" + e;
        }
        return "redirect:/projects/" + projectId;

    }

    @PostMapping("/withdraw")
    public String createWithdrawOperation(HttpServletRequest request,
                                         @RequestParam("project_id") Long projectId) {

        log.info("post /api/operation/withdraw: post operation WITHDRAW with project_id = {}", projectId);

        Optional<Long> clientId = CrowdfundingUtils.getClientIdFromRequestContext();
        if (!clientId.isPresent()) {
            log.warn("Can't get user id from RequestContext");
            return "createProject";
        }
        log.info("Get clientId = {} from security context", clientId.get());
        Long tokenClientId = clientId.get();

        try {
            operationService.withdrawMoneyFromProject(projectId, tokenClientId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/projects/" + projectId + "?error=" + e;
        }
        return "redirect:/projects/" + projectId;

    }

}