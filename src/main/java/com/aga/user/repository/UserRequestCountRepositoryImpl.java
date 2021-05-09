package com.aga.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Repository
@Slf4j
public class UserRequestCountRepositoryImpl implements UserRequestCountRepository {

    private static final String FUNCTION_NAME = "users.update_request_count";
    private static final String IN_LOGIN = "inLogin";
    private static final String OUT_COUNTER = "outReqCount";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void callUserCounterFunction(String login) {
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(FUNCTION_NAME);

        List<Function<StoredProcedureQuery, StoredProcedureQuery>> paramsList = new ArrayList<>();

        paramsList.add(q -> q.registerStoredProcedureParameter(IN_LOGIN, String.class, ParameterMode.IN));
        paramsList.add(q -> q.registerStoredProcedureParameter(OUT_COUNTER, Long.class, ParameterMode.OUT));
        paramsList.add(q -> q.setParameter(IN_LOGIN, login));

        paramsList.forEach( f -> f.apply(storedProcedureQuery));

        storedProcedureQuery.execute();

        long updatedRequestCounter = (long) storedProcedureQuery.getOutputParameterValue(OUT_COUNTER);

        log.debug("Updated requestCounter for login: {} - {}", login, updatedRequestCounter);
    }
}
