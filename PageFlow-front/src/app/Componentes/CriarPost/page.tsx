"use client";

import { useState } from "react";
import {
  PostContainer,
  TextArea,
  Divider,
  ActionsRow,
  ActionButton,
  SendButton,
} from "./styles";
import { createPost } from "../../API/WritePost/WritePostApi";

import { Montserrat, Poppins } from "next/font/google";
export const montserrat = Montserrat({ subsets: ["latin"], weight: ["400", "700"] });
export const poppins = Poppins({ subsets: ["latin"], weight: ["400", "700"] });

export default function CriarPost() {
  const [title, setTitle] = useState("");
  const [post, setPost] = useState("");

  const handleSend = async () => {
    if (post.trim() === "" || title.trim() === "") return;

    const response = await createPost({
      title,
      content: post,
      categoryId: 1,
    });

    if (response) {
      console.log("Post criado com sucesso:");
      setPost("");
      setTitle("");
    }
  };

  return (
    <div className={poppins.className}>
      <PostContainer>
        <input
          type="text"
          placeholder="Title..."
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="w-full bg-transparent resize-none outline-none text-gray-700 placeholder-gray-500 mb-2"
        />

        <TextArea
          placeholder="Write a post..."
          value={post}
          onChange={(e) => setPost(e.target.value)}
        />

        <Divider />

        <ActionsRow>
          <div className="flex gap-2">
            <ActionButton>
              <i className="fas fa-image mr-2" />
              Pictures
            </ActionButton>
            <ActionButton>
              <i className="fas fa-map-marker-alt mr-2" />
              Location
            </ActionButton>
          </div>

          <SendButton onClick={handleSend}>
            <i className="fas fa-paper-plane" />
          </SendButton>
        </ActionsRow>
      </PostContainer>
    </div>
  );
}
