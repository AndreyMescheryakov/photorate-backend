package com.vsu.demo.repository;

import com.vsu.demo.entity.Photo;
import com.vsu.demo.exception.ValidationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PhotoRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PhotoRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Photo> findAll(int page, int size) {
        String sql = "SELECT * FROM photos ORDER BY created_at DESC LIMIT :size OFFSET :offset";
        return namedParameterJdbcTemplate.query(sql, Map.of("size", size, "offset", page * size), PHOTO_ROW_MAPPER);
    }

    public Optional<Photo> findById(UUID id) {
        String sql = "SELECT * FROM photos WHERE id = :id";
        List<Photo> photos = namedParameterJdbcTemplate.query(sql, Map.of("id", id), PHOTO_ROW_MAPPER);
        if (photos.size() > 1) {
            throw new ValidationException("More than one photo with id " + id);
        }
        return photos.stream().findFirst();
    }

    public List<Photo> findByTitle(String title) {
        String sql = "SELECT * FROM photos WHERE LOWER(title) LIKE LOWER(:title)";
        return namedParameterJdbcTemplate.query(sql, Map.of("title", "%" + title + "%"), PHOTO_ROW_MAPPER);
    }

    public List<Photo> findByUserId(UUID userId) {
        String sql = "SELECT * FROM photos WHERE user_id = :userId";
        return namedParameterJdbcTemplate.query(sql, Map.of("userId", userId), PHOTO_ROW_MAPPER);
    }

    public Boolean create(Photo photo) {
        String sql = "INSERT INTO photos (id, title, description, category, created_at, user_id, photo_file_id, version) " +
                "VALUES (:id, :title, :description, :category, :createdAt, :userId, :photoFileId, :version)";
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("id", photo.getId());
        params.put("title", photo.getTitle());
        params.put("description", photo.getDescription());
        params.put("category", photo.getCategory());
        params.put("createdAt", photo.getCreatedAt());
        params.put("userId", photo.getUserId());
        params.put("photoFileId", photo.getPhotoFileId());
        params.put("version", photo.getVersion());
        return namedParameterJdbcTemplate.update(sql, params) == 1;
    }

    public Boolean update(Photo photo) {
        String sql = "UPDATE photos SET title = :title, description = :description, category = :category, " +
                "version = version + 1 WHERE id = :id AND version = :version";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", photo.getId(),
                "title", photo.getTitle(),
                "description", photo.getDescription(),
                "category", photo.getCategory(),
                "version", photo.getVersion()
        )) == 1;
    }

    public Boolean deleteById(UUID id) {
        String sql = "DELETE FROM photos WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Map.of("id", id)) == 1;
    }

    private static final RowMapper<Photo> PHOTO_ROW_MAPPER = (rs, rowNum) -> new Photo(
            rs.getObject("id", UUID.class),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("category"),
            rs.getDate("created_at").toLocalDate(),
            rs.getObject("user_id", UUID.class),
            rs.getObject("photo_file_id", UUID.class),
            rs.getInt("version")
    );
}
