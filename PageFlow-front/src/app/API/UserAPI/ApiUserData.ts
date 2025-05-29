const API_BASE_URL = "http://localhost:8080";

export interface BlogUser {
  id: number;
  name: string;
  email: string;
  username: string;
  role: string;
  userType: string;
}

export const fetchUserData = async (): Promise<BlogUser | null> => {
  const token = localStorage.getItem("token");

  if (!token) {
    console.error("Token ou userId não encontrado no localStorage.");
    return null;
  }

  try {
    const decoded = parseJwt(token);
    const response = await fetch(`${API_BASE_URL}/api/v1/users/${decoded.id}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) throw new Error("Erro ao buscar usuário.");

    const user = await response.json();
    return user as BlogUser;
  } catch (error) {
    console.error("Erro ao buscar dados do usuário:", error);
    return null;
  }
};

// 🔍 Função para decodificar o token JWT
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
    console.error("Erro ao decodificar o token:", e);
    return null;
  }
}
