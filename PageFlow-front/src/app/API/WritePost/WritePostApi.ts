const API_BASE_URL = "http://localhost:8080";

interface CreatePostPayload {
  title: string;
  content: string;
}

interface PostResponse {
  id: number;
  title: string;
  content: string;
  authorId: number;
  categoryId: number;
  createdAt?: string;
  updatedAt?: string;
}

export const createPost = async (
  payload: CreatePostPayload
): Promise<PostResponse | null> => {
  const token = localStorage.getItem("token");

  if (!token) {
    console.error("Token não encontrado.");
    return null;
  }

  const decoded = parseJwt(token);
  const authorId = decoded?.id;

  if (!authorId) {
    console.error("ID do usuário não encontrado no token.");
    return null;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/new`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: payload.title,
        content: payload.content,
        authorId: authorId,
        categoryId: 1,
      }),
    });

    if (!response.ok) throw new Error("Erro ao criar o post.");

    const data: PostResponse = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao enviar post:", error);
    return null;
  }
};

// 🔍 JWT Decode
function parseJwt(token: string) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );

    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error("Erro ao decodificar token:", e);
    return null;
  }
}
