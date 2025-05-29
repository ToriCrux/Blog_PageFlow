const API_BASE_URL = "http://localhost:8080";

export const deletePostById = async (postId: number): Promise<boolean> => {
  const token = localStorage.getItem("token");

  if (!token) {
    console.error("Token não encontrado.");
    return false;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${postId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response.status === 204;
  } catch (error) {
    console.error("Erro ao deletar post:", error);
    return false;
  }
};
