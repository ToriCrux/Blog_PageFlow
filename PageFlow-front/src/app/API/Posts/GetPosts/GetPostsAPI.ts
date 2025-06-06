const API_BASE_URL = "http://localhost:8080";

export interface PostData {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  author: {
    id: number;
    name: string;
    username: string;
    email: string;
    role: string;
    bio: string;
  };
  category: {
    id: number;
    name: string;
  };
  comments: [
    {
      id: number;
      content: string;
      approved: boolean;
      createdAt: string;
      updatedAt: string;
    }
  ];
  status: string;
}

export const getAllPosts = async (): Promise<PostData[] | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts`);

    if (!response.ok) throw new Error("Erro ao buscar posts");

    const posts = await response.json();

    return posts as PostData[];
  } catch (error) {
    console.error("Erro ao buscar posts:", error);

    return null;
  }
};
