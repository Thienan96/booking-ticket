package com.example.booking.entity.audit;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditMetadata {
  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = "created_date", updatable = false)
  private LocalDateTime createdDate;

  @LastModifiedBy
  @Column(name = "last_modified_by")
  private String lastModifiedBy;

  @LastModifiedDate
  @Column(name = "last_modified_date")
  private LocalDateTime lastModifiedDate;
}
