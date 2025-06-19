'use client';

import { poppins } from "@/app/fonts";
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
      <div className="relative h-full ml-[15%]">
        <div className="absolute top-full translate-y-[-50%]">
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
          {/* Renderiza somente se o user estiver carregado */}
          {user && <UserPostsOnly userId={String(user.id)} />}
        </div>
      </main>
    </>
  );
}

function UserPostsOnly({ userId }: { userId: string }) {
  const {
    posts,
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

  const myPosts = posts.filter((post) => post.author.id === Number(userId));


  if (myPosts.length === 0) {
    return (
      <p className={`${poppins.className} text-center mt-8 text-[#9C0D38]`}>
        Você ainda não publicou nenhum post.
      </p>
    );
  }

  return (
    <div className="mt-8">
      {myPosts.map((post) => (
        <PostUnit
          key={post.id}
          post={post}
          userId={Number(userId)} 
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
