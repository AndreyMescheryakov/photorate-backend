package com.vsu.demo.repository;

import com.vsu.demo.entity.Rating;
import com.vsu.demo.response.RatingAverage;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class RatingRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RatingRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Boolean create(Rating rating) {
        String sql = "INSERT INTO ratings (id, visual_appeal, photo_quality, style, created_at, user_id, photo_id) " +
                "VALUES (:id, :visualAppeal, :photoQuality, :style, :createdAt, :userId, :photoId)";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", rating.getId(),
                "visualAppeal", rating.getVisualAppeal(),
                "photoQuality", rating.getPhotoQuality(),
                "style", rating.getStyle(),
                "createdAt", rating.getCreatedAt(),
                "userId", rating.getUserId(),
                "photoId", rating.getPhotoId()
        )) == 1;
    }

    public boolean existsByUserAndPhoto(UUID userId, UUID photoId) {
        String sql = "SELECT count(*) FROM ratings WHERE user_id = :userId AND photo_id = :photoId";
        Integer count = namedParameterJdbcTemplate.queryForObject(sql,
                Map.of("userId", userId, "photoId", photoId), Integer.class);
        return count != null && count > 0;
    }

    public RatingAverage averageByPhoto(UUID photoId) {
        String sql = "SELECT avg(visual_appeal) AS va, avg(photo_quality) AS pq, avg(style) AS st " +
                "FROM ratings WHERE photo_id = :photoId";
        return namedParameterJdbcTemplate.queryForObject(sql, Map.of("photoId", photoId), (rs, rowNum) -> {
            double va = rs.getDouble("va");
            double pq = rs.getDouble("pq");
            double st = rs.getDouble("st");
            double overall = (va + pq + st) / 3.0;
            return new RatingAverage(va, pq, st, overall);
        });
    }

    public List<Rating> findByPhoto(UUID photoId) {
        String sql = "SELECT * FROM ratings WHERE photo_id = :photoId";
        return namedParameterJdbcTemplate.query(sql, Map.of("photoId", photoId), RATING_ROW_MAPPER);
    }

    private static final RowMapper<Rating> RATING_ROW_MAPPER = (rs, rowNum) -> new Rating(
            rs.getObject("id", UUID.class),
            rs.getInt("visual_appeal"),
            rs.getInt("photo_quality"),
            rs.getInt("style"),
            rs.getDate("created_at").toLocalDate(),
            rs.getObject("user_id", UUID.class),
            rs.getObject("photo_id", UUID.class)
    );
}
