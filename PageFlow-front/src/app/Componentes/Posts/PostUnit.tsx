"use client";

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

import { useState, ChangeEvent } from "react";
import { poppins } from "../../fonts";
import { PostData } from "@/app/API/Posts/GetPosts/GetPostsAPI";

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
  commentInput,
  setEditedTitle,
  setEditedContent,
  setCommentInput,
  handleDelete,
  handleEdit,
  handleSubmitEdit,
  handleCommentSubmit,
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

  const handleCommentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    setCommentInput((prev: Record<number, string>) => {
      return {
        ...prev,
        [post.id]: e.target.value,
      };
    });
  };

  return (
    <div className={poppins.className}>
      <PostWrapper>
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
              <InputStyled value={editedTitle} onChange={handleTitleChange} />
              <TextareaStyled value={editedContent} onChange={handleContentChange} />
            </>
          ) : (
            <>
              <PostTitle>{post.title}</PostTitle>
              <PostContent>{post.content}</PostContent>
            </>
          )}

          <div className="mt-4">
            <div
              className="flex items-center gap-2 cursor-pointer text-sm text-gray-600 font-semibold"
              onClick={() => toggleComments(post.id)}
            >
              💬 {post.comments?.length ?? 0} comentário{(post.comments?.length !== 1 ? "s" : "")}
              <span>{visibleComments[post.id] ? "🔼" : "🔽"}</span>
            </div>

            {visibleComments[post.id] && (
              <ul className="text-sm text-gray-800 pl-4 list-disc mt-1">
                {(post.comments ?? []).map((comment, index) => (
                  <li key={index}>{comment.content}</li>
                ))}
              </ul>
            )}
          </div>
        </PostBody>

        <PostFooter>
          {editingPostId === post.id ? (
            <SendEditIcon onClick={() => handleSubmitEdit(post.id)}>
              <i className="fas fa-paper-plane" />
            </SendEditIcon>
          ) : (
            <>
              <CommentBox
                placeholder="Write a comment..."
                value={commentInput[post.id] || ""}
                onChange={handleCommentChange}
              />
              <SendEditIcon onClick={() => handleCommentSubmit(post.id)}>
                <i className="fas fa-paper-plane" />
              </SendEditIcon>
            </>
          )}
        </PostFooter>
      </PostWrapper>
    </div>
  );
}
