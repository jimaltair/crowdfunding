package ru.pcs.crowdfunding.tran.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pcs.crowdfunding.tran.domain.OperationType;

public interface OperationTypesRepository extends JpaRepository<OperationType, Long> {

    @Query(value = "select * from operation_type where type = ?1", nativeQuery=true)
    OperationType findOperationType(String type);
}
