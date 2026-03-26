package com.project.DATN2026.entity;

import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Size")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Size implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Nationalized
    private String name;
    private Boolean deleteFlag;
}