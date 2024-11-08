package com.example.booking.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.booking.entity.audit.AuditMetadata;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ticket_code")
@DynamicInsert
@DynamicUpdate
public class TicketCodeEntity extends AuditMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_category_id", referencedColumnName = "id", nullable = false)
    private TicketEntity ticket;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "ticket_code_user", 
      joinColumns = 
        { @JoinColumn(name = "ticket_code_id", referencedColumnName = "id") },
      inverseJoinColumns = 
        { @JoinColumn(name = "user_id", referencedColumnName = "id") })
    private UserEntity user;

}