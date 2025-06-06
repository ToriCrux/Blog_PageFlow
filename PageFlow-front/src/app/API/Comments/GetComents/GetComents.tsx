// src/API/GetCommentsAPI.ts

const API_BASE_URL = "http://localhost:8080";

export interface CommentData {
  content: string;
  approved: boolean;
  updatedAt: string;
}

export const getAllComments = async (): Promise<CommentData[]> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) throw new Error("Token não encontrado");

    const response = await fetch(`${API_BASE_URL}/api/v1/comments`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error("Erro ao buscar comentários");

    const comments = await response.json();
    return comments;
  } catch (error) {
    console.error("Erro ao buscar comentários:", error);
    return [];
  }
};
