package com.blog.api.repository;

import com.blog.api.models.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataRepository extends JpaRepository<Metadata, Long> {

}
