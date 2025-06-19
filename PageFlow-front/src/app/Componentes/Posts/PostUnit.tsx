"use client";

import {
  PostWrapper,
  PostHeader,
  PostBody,
  PostFooter,
  AuthorImage,
  PostTitle,
  PostContent,
  DeleteIcon,
  EditIcon,
  SendEditIcon,
  InputStyled,
  TextareaStyled,
} from "./styles";

import { useState, ChangeEvent } from "react";
import { poppins } from "../../fonts";
import { PostData } from "@/app/API/Posts/GetPosts/GetPostsAPI";
import { ToggleCheckbox } from "../CriarPost/styles";

type Props = {
  post: PostData;
  userId: number | null;
  editingPostId: number | null;
  editedTitle: string;
  editedContent: string;
  commentInput: Record<number, string>;
  setEditedTitle: (val: string) => void;
  setEditedContent: (val: string) => void;
  setCommentInput: (val: Record<number, string>) => void;
  handleDelete: (id: number) => void;
  handleEdit: (post: PostData) => void;
  handleSubmitEdit: (id: number) => void;
  handleCommentSubmit: (id: number) => void;
};

export default function PostUnit({
  post,
  userId,
  editingPostId,
  editedTitle,
  editedContent,
  setEditedTitle,
  setEditedContent,
  handleDelete,
  handleEdit,
  handleSubmitEdit,
}: Props) {
  const [visibleComments, setVisibleComments] = useState<Record<number, boolean>>({});

  const toggleComments = (postId: number) => {
    setVisibleComments((prev: Record<number, boolean>) => ({
      ...prev,
      [postId]: !prev[postId],
    }));
  };

  const handleTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEditedTitle(e.target.value);
  };

  const handleContentChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    setEditedContent(e.target.value);
  };

  return (
    <div className={poppins.className}>
      <PostWrapper>
        <PostHeader>
          <AuthorImage />
          <div className="font-bold">{post.author.name}</div>

          {post.author.id === userId && (
            <>
              <EditIcon onClick={() => handleEdit(post)}>
                <i className="fa-solid fa-pen-to-square"></i>
              </EditIcon>
              <DeleteIcon onClick={() => handleDelete(post.id)}>
                <i className="fa-solid fa-trash"></i>
              </DeleteIcon>
            </>
          )}
        </PostHeader>

        <PostBody>
          {editingPostId === post.id ? (
            <>
              <InputStyled value={editedTitle} onChange={handleTitleChange} />
              <TextareaStyled value={editedContent} onChange={handleContentChange} />
            </>
          ) : (
            <>
              <PostTitle>{post.title}</PostTitle>
              <PostContent dangerouslySetInnerHTML={{ __html: post.content }} />
            </>
          )}

          <div className="mt-4">
            <div
              className="flex items-center gap-2 cursor-pointer text-sm text-gray-600 font-semibold"
              onClick={() => toggleComments(post.id)}
            >
              <i className="fas fa-solid fa-comment mr-2" />
              {post.comments?.length ?? 0} comentário{post.comments?.length !== 1 ? "s" : ""}
              <span>
                {visibleComments[post.id] ? (
                  <i className="fas fa-solid fa-square-caret-up ml-2"></i>
                ) : (
                  <i className="fas fa-solid fa-square-caret-down ml-2"></i>
                )}
              </span>
            </div>

            {visibleComments[post.id] && (
              <>
                <div className="w-full h-[2px] bg-[#9C0D38] my-2 rounded" />

                <div className="mt-2 space-y-3">
                  {(post.comments ?? []).map((comment, index) => (
                    <div
                      key={index}
                      className="bg-white p-3 rounded-md shadow-sm text-sm text-gray-800"
                    >
                      <div className="flex items-center gap-3 mb-1">
                        <div className="w-6 h-6 rounded-full bg-gray-400" />
                        {comment.content}
                      </div>
                    </div>
                  ))}
                </div>
              </>
            )}
          </div>
        </PostBody>

        <PostFooter>
          {editingPostId === post.id ? (
            <SendEditIcon onClick={() => handleSubmitEdit(post.id)}>
              <i className="fas fa-paper-plane" />
            </SendEditIcon>
          ) : (
            <ToggleCheckbox $checked={post.status === "DRAFT"}>
              {post.status === "DRAFT" ? "Rascunho Ativo" : "Marcar como Rascunho"}
            </ToggleCheckbox>
          )}
        </PostFooter>
      </PostWrapper>
    </div>
  );
}
