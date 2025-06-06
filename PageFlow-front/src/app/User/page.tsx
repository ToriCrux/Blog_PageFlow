'use client';

import { useState, useEffect } from "react";
import Image from "next/image";
import BarraEsquerda from "../Componentes/BarraEsquerda/page";
import NavBar from "../Componentes/NavBar/page";
import { BarraSuperior } from "./styles";
import { BlogUser, fetchUserData } from "@/app/API/UserAPI/ApiUserData";

import { usePostContainer } from "../Componentes/Posts/usePostContainer";
import PostUnit from "../Componentes/Posts/PostUnit";



export default function User() {
  const [user, setUser] = useState<BlogUser | null>(null);

  useEffect(() => {
    const loadUser = async () => {
      const data = await fetchUserData();
      setUser(data);
    };
    loadUser();
  }, []);

  return (
    <>
      <NavBar />
      <BarraEsquerda />

      <BarraSuperior>
        <div className="relative h-full" style={{ marginLeft: '15%' }}>
          <div
            className="absolute"
            style={{
              top: '100%',
              transform: 'translateY(-50%)',
            }}
          >
            <Image
              src="/Img_User_Page.svg"
              alt="Usuário"
              width={200}
              height={200}
            />
          </div>

          <div className="flex items-end h-full pl-[220px]">
            <span className="text-white text-2xl font-semibold">
              {user ? user.name : "Carregando..."}
            </span>
          </div>
        </div>
      </BarraSuperior>

      <main className="pt-20 pl-20 pr-4">
        <div className="max-w-2xl mx-auto">
          <UserPostsOnly />
        </div>
      </main>
    </>
  );
}

function UserPostsOnly() {
  const {
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
  } = usePostContainer();

  if (!userId) return <p>Carregando posts do usuário...</p>;

  const myPosts = posts.filter((post) => post.author.id === userId);

  if (myPosts.length === 0) {
    return <p className="text-center mt-8 text-gray-600">Você ainda não publicou nenhum post.</p>;
  }

  return (
    <div className="mt-8">
      {myPosts.map((post) => (
        <PostUnit
          key={post.id}
          post={post}
          userId={userId}
          editingPostId={editingPostId}
          editedTitle={editedTitle}
          editedContent={editedContent}
          commentInput={commentInput}
          setEditedTitle={setEditedTitle}
          setEditedContent={setEditedContent}
          setCommentInput={setCommentInput}
          handleDelete={handleDelete}
          handleEdit={handleEdit}
          handleSubmitEdit={handleSubmitEdit}
          handleCommentSubmit={handleCommentSubmit}
        />
      ))}
    </div>
  );
}
