package com.vsu.demo.repository;

import com.vsu.demo.entity.PhotoFile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class PhotoFileRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PhotoFileRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Boolean create(PhotoFile file) {
        String sql = "INSERT INTO photo_files (id, file_name, file_content, file_extension, file_size_kb, upload_date) " +
                "VALUES (:id, :fileName, :fileContent, :fileExtension, :fileSizeKb, :uploadDate)";
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("id", file.getId());
        params.put("fileName", file.getFileName());
        params.put("fileContent", file.getFileContent());
        params.put("fileExtension", file.getFileExtension());
        params.put("fileSizeKb", file.getFileSizeKb());
        params.put("uploadDate", file.getUploadDate());
        return namedParameterJdbcTemplate.update(sql, params) == 1;
    }
}
