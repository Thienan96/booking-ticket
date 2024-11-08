package com.example.booking.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.booking.entity.audit.AuditMetadata;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ticket")
@DynamicInsert
@DynamicUpdate
public class TicketEntity extends AuditMetadata implements Serializable {
  private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "valid_date", nullable = false)
    private Date validDate;

    @Column(name = "amount", nullable = true)
    private Integer amount;

    @Column(name = "sold_amount", nullable = true)
    private Integer soldAmount;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    Set<TicketUserEntity> ticketUser;

}