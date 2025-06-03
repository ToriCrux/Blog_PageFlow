// src/API/PostComentsAPI.ts

const API_BASE_URL = "http://localhost:8080";

interface CommentPayload {
  postId: number;
  content: string;
  approved: boolean;
  updatedAt: string;
}

export const postComment = async (comment: CommentPayload): Promise<boolean> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) throw new Error("Token não encontrado");

    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${comment.postId}/comment`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(comment),
    });

    if (!response.ok) throw new Error("Erro ao enviar comentário");

    return true;
  } catch (error) {
    console.error("Erro ao postar comentário:", error);
    return false;
  }
};
