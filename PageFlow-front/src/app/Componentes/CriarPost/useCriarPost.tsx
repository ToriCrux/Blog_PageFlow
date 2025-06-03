// Componentes/CriarPost/useCriarPost.ts
"use client";

import { useEffect, useState } from "react";
import { createPost } from "../../API/Posts/WritePost/WritePostApi";
import { getAllCategories, CategoriaData } from "../../API/Categorias/PostCategorias";

export function useCriarPost() {
  const [title, setTitle] = useState("");
  const [post, setPost] = useState("");
  const [categorias, setCategorias] = useState<CategoriaData[]>([]);
  const [selectedCategoria, setSelectedCategoria] = useState<number | null>(null);

  useEffect(() => {
    const fetchCategorias = async () => {
      const data = await getAllCategories();
      setCategorias(data);
      if (data.length > 0) setSelectedCategoria(data[0].id);
    };
    fetchCategorias();
  }, []);

  const handleSend = async () => {
    if (!selectedCategoria || post.trim() === "" || title.trim() === "") return;

    const response = await createPost({
      title,
      content: post,
      categoryId: selectedCategoria,
    });

    if (response) {
      console.log("Post criado com sucesso:", response);
      setPost("");
      setTitle("");
      setSelectedCategoria(categorias[0]?.id || null);

      // Atualiza os comentários do post não é a melhor prática,
      // mas para fins de simplicidade, vamos fazer isso.
      // Caso tenhamos tempo refatorar
      window.location.reload();
    }
  };

  return {
    title,
    setTitle,
    post,
    setPost,
    categorias,
    selectedCategoria,
    setSelectedCategoria,
    handleSend,
  };
}
