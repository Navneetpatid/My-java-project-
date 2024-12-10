package com.janaushadhi.adminservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "module")
public class Module {


    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private  String module;
    private  String description;
}
