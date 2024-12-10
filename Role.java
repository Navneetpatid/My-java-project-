package com.janaushadhi.adminservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    @NonNull
    private  String role;
    private String description;
    private Date createdDate;
    private Date uptedDate;
    private  Short status;
}
