package ru.template.storage.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6999449532134110309L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private Long createdAt = ZonedDateTime.now(Clock.systemUTC()).toInstant().toEpochMilli();

    @Column(nullable = false, updatable = true)
    private Long updatedAt = ZonedDateTime.now(Clock.systemUTC()).toInstant().toEpochMilli();

}
