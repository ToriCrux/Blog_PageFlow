// src/API/PostCategorias.tsx

const API_BASE_URL = "http://localhost:8080";

export interface CategoriaData {
  id: number;
  name: string;
}

export const getAllCategories = async (): Promise<CategoriaData[]> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) throw new Error("Token não encontrado");

    const response = await fetch(`${API_BASE_URL}/api/v1/categories`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error("Erro ao buscar categorias");

    const categorias = await response.json();

    return categorias;
  } catch (error) {
    console.error("Erro ao buscar categorias:", error);
    return [];
  }
};
