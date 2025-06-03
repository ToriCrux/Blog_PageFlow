'use client';

import { useState, useEffect } from "react";
import Image from "next/image";
import BarraEsquerda from "../Componentes/BarraEsquerda/page";
import NavBar from "../Componentes/NavBar/page";
import { BarraSuperior } from "./styles";
import { BlogUser, fetchUserData } from "@/app/API/UserAPI/ApiUserData";

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
                {/* ✅ Imagem com topo centralizado na barra */}
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

                {/* ✅ Nome do usuário, com margem à esquerda da imagem */}
                <div className="flex items-end h-full pl-[220px]">
                <span className="text-white text-2xl font-semibold">
                    {user ? user.name : "Carregando..."}
                </span>
                </div>
            </div>
        </BarraSuperior>

        <main className="pt-20 pl-20 pr-4">
            <div className="max-w-2xl mx-auto">
                {/* Conteúdo da página */}
            </div>
        </main>
    </>
  );
}
