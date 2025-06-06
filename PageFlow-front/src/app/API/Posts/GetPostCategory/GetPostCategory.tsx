// src/API/Posts/GetPosts/GetPostCategory.ts

const API_BASE_URL = "http://localhost:8080";

import { PostData } from "../GetPosts/GetPostsAPI";

export const getPostsByCategory = async (
  categoryName: string
): Promise<PostData[] | null> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) throw new Error("Token não encontrado");

    const response = await fetch(`${API_BASE_URL}/api/v1/posts/search/${categoryName}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error("Erro ao buscar posts da categoria");

    const posts = await response.json();
    return posts as PostData[];
  } catch (error) {
    console.error("Erro ao buscar posts da categoria:", error);
    return null;
  }
};
