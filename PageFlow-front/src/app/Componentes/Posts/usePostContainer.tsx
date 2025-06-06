"use client";

import { useEffect, useState } from "react";

import { getAllPosts, PostData } from "../../API/Posts/GetPosts/GetPostsAPI";
import { deletePostById } from "../../API/Posts/DeletePost/DeletePost";
import { updatePost } from "../../API/Posts/PutPost/EditarPost";
import { postComment } from "../../API/Comments/PostComents/PostComentsAPI";
import { getPostsByCategory } from "../../API/Posts/GetPostCategory/GetPostCategory";
import { parseJwt } from "./parseJwt";

export function usePostContainer(categoryName?: string) {
  const [posts, setPosts] = useState<PostData[]>([]);
  const [userId, setUserId] = useState<number | null>(null);
  const [editingPostId, setEditingPostId] = useState<number | null>(null);
  const [editedTitle, setEditedTitle] = useState("");
  const [editedContent, setEditedContent] = useState("");
  const [commentInput, setCommentInput] = useState<Record<number, string>>({});

  useEffect(() => {
    const fetchData = async () => {
      const stored = sessionStorage.getItem("postsByCategory");
      if (stored) {
        const parsed = JSON.parse(stored);
        setPosts(parsed);
        sessionStorage.removeItem("postsByCategory");
        return;
      }

      const postsData = categoryName
        ? await getPostsByCategory(categoryName)
        : await getAllPosts();

      if (postsData) setPosts(postsData);
    };

    const token = localStorage.getItem("token");
    if (token) {
      const decoded = parseJwt(token);
      setUserId(decoded?.id || null);
    }

    fetchData();
  }, [categoryName]);

  const handleDelete = async (postId: number) => {
    if (!confirm("Deseja realmente excluir este post?")) return;

    const success = await deletePostById(postId);
    if (success) {
      setPosts((prev) => prev.filter((p) => p.id !== postId));
    }
  };

  const handleEdit = (post: PostData) => {
    setEditingPostId(post.id);
    setEditedTitle(post.title);
    setEditedContent(post.content);
  };

  const handleSubmitEdit = async (postId: number) => {
    const success = await updatePost({
      id: postId,
      title: editedTitle,
      content: editedContent,
      categoryId: 1,
    });

    if (success) {
      setPosts((prev) =>
        prev.map((p) =>
          p.id === postId ? { ...p, title: editedTitle, content: editedContent } : p
        )
      );
      setEditingPostId(null);
    }
  };

  const handleCommentSubmit = async (postId: number) => {
    const content = commentInput[postId]?.trim();
    if (!content) return;

    const success = await postComment({
      postId,
      content,
      approved: true,
      updatedAt: new Date().toISOString(),
    });

    if (success) {
      alert("Comentário enviado!");
      setCommentInput((prev) => ({ ...prev, [postId]: "" }));
      window.location.reload();
    } else {
      alert("Erro ao enviar comentário.");
    }
  };

  return {
    posts,
    userId,
    editingPostId,
    editedTitle,
    editedContent,
    commentInput,
    setEditedTitle,
    setEditedContent,
    setCommentInput,
    handleDelete,
    handleEdit,
    handleSubmitEdit,
    handleCommentSubmit,
  };
}
