package com.vsu.demo.repository;

import com.vsu.demo.entity.Comment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CommentRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CommentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Comment create(Comment comment) {
        String sql = "INSERT INTO comments (text, created_at, user_id, photo_id) " +
                "VALUES (:text, :createdAt, :userId, :photoId)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("text", comment.getText())
                .addValue("createdAt", comment.getCreatedAt())
                .addValue("userId", comment.getUserId())
                .addValue("photoId", comment.getPhotoId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        Integer id = keyHolder.getKey().intValue();
        return new Comment(id, comment.getText(), comment.getCreatedAt(), comment.getUserId(), comment.getPhotoId());
    }

    public Optional<Comment> findById(Integer id) {
        String sql = "SELECT * FROM comments WHERE id = :id";
        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), COMMENT_ROW_MAPPER).stream().findFirst();
    }

    public List<Comment> findByPhoto(UUID photoId) {
        String sql = "SELECT * FROM comments WHERE photo_id = :photoId ORDER BY created_at DESC";
        return namedParameterJdbcTemplate.query(sql, Map.of("photoId", photoId), COMMENT_ROW_MAPPER);
    }

    public Boolean deleteById(Integer id) {
        String sql = "DELETE FROM comments WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Map.of("id", id)) == 1;
    }

    private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, rowNum) -> new Comment(
            rs.getInt("id"),
            rs.getString("text"),
            rs.getDate("created_at").toLocalDate(),
            rs.getObject("user_id", UUID.class),
            rs.getObject("photo_id", UUID.class)
    );
}
