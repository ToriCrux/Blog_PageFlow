const API_BASE_URL = "http://localhost:8080";

export interface UpdatePostPayload {
  id: number;
  title: string;
  content: string;
  categoryId: number;
}

export const updatePost = async (payload: UpdatePostPayload): Promise<boolean> => {
  const token = localStorage.getItem("token");

  if (!token) {
    console.error("Token não encontrado.");
    return false;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${payload.id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: payload.title,
        content: payload.content,
        authorId: JSON.parse(atob(token.split(".")[1])).id,
        categoryId: payload.categoryId,
      }),
    });

    return response.ok;
  } catch (error) {
    console.error("Erro ao atualizar post:", error);
    return false;
  }
};
