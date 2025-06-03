"use client";

import Head from "next/head";
import NavBar from "../Componentes/NavBar/page";
import BarraEsquerda from "../Componentes/BarraEsquerda/page";
import CriarPost from "../Componentes/CriarPost/page";
import PostContainer from "../Componentes/Posts/PostContainer"; 

export default function Home() {
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
          <PostContainer />
        </div>
      </main>
    </>
  );
}
