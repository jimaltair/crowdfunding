package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.ProjectStatus;
import ru.pcs.crowdfunding.client.dto.OperationDto;
import ru.pcs.crowdfunding.client.repositories.ProjectStatusesRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final TransactionServiceClient transactionServiceClient;
    private final ClientsService clientsService;
    private final ProjectsService projectsService;
    private final ProjectStatusesRepository projectStatusesRepository;

    @Override
    public OperationDto operate(OperationDto operationDto) throws IllegalArgumentException {
        return transactionServiceClient.operate(operationDto);
    }

    @Override
    @Transactional
    public void withdrawMoneyFromProject(Long projectId, Long clientId) throws IllegalArgumentException {

        Long projectAuthor = projectsService.getAuthorByProjectId(projectId);

        if(projectAuthor != clientId) {
            throw new IllegalArgumentException("Withdraw money can only the author!");
        }

        Long clientAccountId = clientsService.getAccountIdByClientId(clientId);
        Long projectAccountId = projectsService.getAccountIdByProjectId(projectId);

        BigDecimal moneyCollected = projectsService.getMoneyCollectedByProjectId(projectId);
        BigDecimal moneyGoal = projectsService.getMoneyGoal(projectId);

        if(moneyCollected.compareTo(moneyGoal) < 0 ) {
            throw new IllegalArgumentException("The money goal not achieved!");
        }

        OperationDto operationDto = OperationDto.builder()
                .operationType(OperationDto.Type.PAYMENT)
                .sum(moneyCollected)
                .initiatorId(clientId)
                .debitAccountId(clientAccountId)
                .creditAccountId(projectAccountId)
                .build();

        operationDto = operate(operationDto);
        log.info("Getting operationDto from tran service = {} ", operationDto);

        projectsService.setProjectStatus(projectId,
                projectStatusesRepository.getByStatus(ProjectStatus.Status.FINISHED));
    }

    @Override
    @Transactional
    public void paymentToProject(Long projectId, Long clientId, BigDecimal sumPayment) throws IllegalArgumentException {

        Long clientAccountId = clientsService.getAccountIdByClientId(clientId);
        Long projectAccountId = projectsService.getAccountIdByProjectId(projectId);


        OperationDto operationDto = OperationDto.builder()
                .operationType(OperationDto.Type.PAYMENT)
                .sum(sumPayment)
                .initiatorId(clientId)
                .debitAccountId(projectAccountId)
                .creditAccountId(clientAccountId)
                .build();

        operationDto = operate(operationDto);
        log.info("Getting operationDto from tran service = {} ", operationDto);

    }

    @Override
    @Transactional
    public void topUpToClient(Long clientId, BigDecimal sumTopUp) throws IllegalArgumentException {

        Long clientAccountId = clientsService.getAccountIdByClientId(clientId);

        OperationDto operationDto = OperationDto.builder()
                .operationType(OperationDto.Type.TOP_UP)
                .sum(sumTopUp)
                .initiatorId(clientId)
                .debitAccountId(clientAccountId)
                .build();

        operationDto = operate(operationDto);
        log.info("Getting operationDto from tran service = {} ", operationDto);

    }

}
