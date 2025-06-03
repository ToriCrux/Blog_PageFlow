// Login/useLogin.ts
"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { loginUsuario } from "./apiServiceLogin";
import { setAuthToken } from "./auth";

export function useLogin() {
  const router = useRouter();
  const [form, setForm] = useState({ email: "", password: "" });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    if (!form.email || !form.password) {
      alert("Preencha todos os campos.");
      return;
    }

    try {
      const result = await loginUsuario(form);

      if (result.jwt) {
        setAuthToken(result.jwt);
        router.push("/Home");
        alert("Login realizado com sucesso!");
      } else {
        throw new Error("Token ou ID não recebido do servidor.");
      }
    } catch (error: unknown) {
      if (error instanceof Error) {
        alert("Erro: " + error.message);
      } else {
        alert("Erro inesperado.");
      }
    }
  };

  return {
    form,
    handleChange,
    handleSubmit,
  };
}
