"use client";

import { useState } from "react";
import Head from "next/head";
import { Suspense } from "react"; // ✅ IMPORTANTE

import NavBar from "../Componentes/NavBar/page";
import BarraEsquerda from "../Componentes/BarraEsquerda/page";
import CriarPost from "../Componentes/CriarPost/page";
import PostContainer from "../Componentes/Posts/PostContainer";
import { poppins } from "../fonts";

export default function Home() {
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <>
      <Head>
        <link rel="icon" href="/favicon.ico" />
        <title>Home | Page Flow</title>
      </Head>

      <NavBar />
      <BarraEsquerda />

      <main className="pt-20 pl-20 pr-4">
        <div className="max-w-2xl mx-auto">
          <CriarPost />

          {/* Barra de Pesquisa */}
          <div className="mb-6">
            <input
              type="text"
              placeholder="Buscar posts..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className={`${poppins.className} w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 transition ease-in-out`}
            />
          </div>

          {/* ✅ ENVOLVER COM SUSPENSE */}
          <Suspense fallback={<div>Carregando posts...</div>}>
            <PostContainer searchTerm={searchTerm} />
          </Suspense>
        </div>
      </main>
    </>
  );
}
