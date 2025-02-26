package br.edu.infnet.pageflow.model;

import jakarta.persistence.Entity;

@Entity
public class Visitor extends User {

    private boolean canComment; // Permissão para comentar ou não

}
