export interface CadastroPayload {
  name: string;
  username: string;
  email: string;
  password: string;
  role: string;
}

const API_BASE_URL = "http://localhost:8080";

export const cadastrarUsuario = async (payload: CadastroPayload) => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/signup`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Erro ao cadastrar usuário (resposta inválida).");
    }

    return await response.json();
  } catch (error: any) {
    if (error.message === "Failed to fetch") {
      throw new Error("Não foi possível conectar ao servidor. Verifique se a API está rodando.");
    }
    throw error;
  }
};
