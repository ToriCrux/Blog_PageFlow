export interface LoginPayload {
  email: string;
  password: string;
}

export interface LoginResponse {
  jwt: string;
}


const API_BASE_URL = "http://localhost:8080";

export const loginUsuario = async (payload: LoginPayload): Promise<LoginResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Erro ao fazer login (resposta inválida).");
    }

    const result = await response.json();
    return result;

  } catch (error) {
    if (error instanceof Error && error.message === "Failed to fetch") {
      throw new Error("Não foi possível conectar ao servidor. Verifique se a API está rodando.");
    }
    throw error;
  }
};
