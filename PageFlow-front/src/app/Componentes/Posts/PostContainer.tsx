"use client";

import { useEffect, useState } from "react";
import { getAllPosts, PostData } from "../../API/GetPosts/GetPostsAPI";
import { deletePostById } from "../../API/DeletePost/DeletePost";
import { updatePost } from "../../API/PutPost/EditarPost";
import {
  PostWrapper,
  PostHeader,
  PostBody,
  PostFooter,
  AuthorImage,
  PostTitle,
  PostContent,
  CommentBox,
  DeleteIcon,
  EditIcon,
  SendEditIcon,
  InputStyled,
  TextareaStyled,
} from "./styles";

import { Montserrat, Poppins } from "next/font/google";
export const montserrat = Montserrat({ subsets: ["latin"], weight: ["400", "700"] });
export const poppins = Poppins({ subsets: ["latin"], weight: ["400", "700"] });

export default function PostContainer() {
  const [posts, setPosts] = useState<PostData[]>([]);
  const [userId, setUserId] = useState<number | null>(null);
  const [editingPostId, setEditingPostId] = useState<number | null>(null);
  const [editedTitle, setEditedTitle] = useState("");
  const [editedContent, setEditedContent] = useState("");

  useEffect(() => {
    const fetchPosts = async () => {
      const data = await getAllPosts();
      if (data) setPosts(data);
    };

    const token = localStorage.getItem("token");
    if (token) {
      const decoded = parseJwt(token);
      setUserId(decoded?.id || null);
    }

    fetchPosts();
  }, []);

  const handleDelete = async (postId: number) => {
    const confirmDelete = confirm("Deseja realmente excluir este post?");
    if (!confirmDelete) return;

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

  return (
    <div className={poppins.className}>
      {posts.map((post) => (
        <PostWrapper key={post.id}>
          <PostHeader>
            <AuthorImage />
            <div className="font-bold">{post.author.name}</div>

            {post.author.id === userId && (
              <>
                <EditIcon onClick={() => handleEdit(post)}>✎</EditIcon>
                <DeleteIcon onClick={() => handleDelete(post.id)}>✖</DeleteIcon>
              </>
            )}
          </PostHeader>

          <PostBody>
            {editingPostId === post.id ? (
              <>
                <InputStyled
                  value={editedTitle}
                  onChange={(e) => setEditedTitle(e.target.value)}
                />
                <TextareaStyled
                  value={editedContent}
                  onChange={(e) => setEditedContent(e.target.value)}
                />
              </>
            ) : (
              <>
                <PostTitle>{post.title}</PostTitle>
                <PostContent>{post.content}</PostContent>
              </>
            )}
          </PostBody>

          <PostFooter>
            {editingPostId === post.id ? (
              <SendEditIcon onClick={() => handleSubmitEdit(post.id)}>
                <i className="fas fa-paper-plane" />
              </SendEditIcon>
            ) : (
              <CommentBox placeholder="Write a comment..." />
            )}
          </PostFooter>
        </PostWrapper>
      ))}
    </div>
  );
}

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
