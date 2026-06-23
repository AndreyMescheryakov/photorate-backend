package com.vsu.demo.repository;

import com.vsu.demo.entity.Role;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return namedParameterJdbcTemplate.query(sql, USER_ROW_MAPPER);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        List<User> users = namedParameterJdbcTemplate.query(sql, Map.of("email", email), USER_ROW_MAPPER);
        if (users.size() > 1) {
            throw new ValidationException("More than one user with email " + email);
        }
        return users.stream().findFirst();
    }

    public Optional<User> findById(UUID id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), USER_ROW_MAPPER).stream().findFirst();
    }

    public void lockOnValue(String value) {
        String sql = "SELECT pg_advisory_xact_lock(hashtext(:lock))";
        namedParameterJdbcTemplate.query(sql, Map.of("lock", value), rs -> {});
    }

    public Boolean create(User user) {
        String sql = "INSERT INTO users (id, email, nickname, password_hash, role, created_at) " +
                "VALUES (:id, :email, :nickname, :passwordHash, :role, :createdAt)";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "passwordHash", user.getPasswordHash(),
                "role", user.getRole().name(),
                "createdAt", user.getCreatedAt()
        )) == 1;
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM users WHERE id = :id";
        if (namedParameterJdbcTemplate.update(sql, Map.of("id", id)) == 0) {
            throw new ValidationException("User not found");
        }
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getObject("id", UUID.class),
            rs.getString("email"),
            rs.getString("nickname"),
            rs.getString("password_hash"),
            Role.valueOf(rs.getString("role")),
            rs.getDate("created_at").toLocalDate()
    );
}
