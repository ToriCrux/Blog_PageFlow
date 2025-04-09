package br.edu.infnet.pageflow.utilitarios;

import java.util.UUID;

public class GeradorDados {

    public static String gerarEmailUnico() {
        return "teste_" + UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";
    }

    public static String gerarNomeUnico() {
        return "usuario_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
