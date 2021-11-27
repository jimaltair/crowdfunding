package ru.pcs.crowdfunding.client.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PongRepositoryImpl implements PongRepository {
    //language=SQL
    private final static String SQL_SELECT_PONG = "SELECT 'Pong from ClientService!';";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String getPong() {
        return jdbcTemplate.queryForObject(SQL_SELECT_PONG, String.class);
    }
}
