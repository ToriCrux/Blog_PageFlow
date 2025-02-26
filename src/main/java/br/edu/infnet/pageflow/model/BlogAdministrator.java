package br.edu.infnet.pageflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class BlogAdministrator extends User {

    @Column(nullable = false)
    private String adminCode; // Código especial para autenticação

// Métodos específicos para administradores
}