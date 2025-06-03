// Cadastro/useCadastro.ts
"use client";

import { useState } from "react";
import { cadastrarUsuario } from "./apiService";
import { validarFormulario } from "./validarFormulario";

export function useCadastro() {
  const [form, setForm] = useState({
    name: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: "AUTHOR",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    const resultado = validarFormulario(form);

    if (!resultado.valido) {
      alert(resultado.mensagens.join("\n"));
      return;
    }

    try {
      await cadastrarUsuario({
        name: form.name,
        username: form.username,
        email: form.email,
        password: form.password,
        role: form.role,
      });

      alert("Usuário cadastrado com sucesso!");

      setForm({
        name: "",
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
        role: "AUTHOR", // corrigido aqui (antes era ADMINISTRATOR)
      });
      } catch (error: unknown) {
        if (error instanceof Error) {
          alert("Erro ao cadastrar usuário: " + error.message);
        } else {
          alert("Erro desconhecido ao cadastrar usuário.");
        }
      }
  };

  return { form, handleChange, handleSubmit };
}
