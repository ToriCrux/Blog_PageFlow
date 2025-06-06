"use client";

import { useState } from "react";
import { useSearchParams } from "next/navigation";
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

import { poppins } from "../../fonts";
import { usePostContainer } from "./usePostContainer";
import DOMPurify from "dompurify";

const purifier = DOMPurify as unknown as { sanitize: (dirty: string) => string };

type PostContainerProps = {
  searchTerm: string;
};

export default function PostContainer({ searchTerm }: PostContainerProps) {
  const searchParams = useSearchParams();
  const category = searchParams.get("category") || undefined;

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
  } = usePostContainer(category);

  const [visibleComments, setVisibleComments] = useState<Record<number, boolean>>({});

  const toggleComments = (postId: number) => {
    setVisibleComments((prev) => ({
      ...prev,
      [postId]: !prev[postId],
    }));
  };

  const filteredPosts = posts.filter(
    (post) =>
      post.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      post.content.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className={poppins.className}>
      {filteredPosts
        .filter((post) => post.status === "PUBLISHED")
        .map((post) => (
          <PostWrapper key={post.id} className="mb-8">
            <PostHeader className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <AuthorImage />
                <div className="font-bold text-lg">{post.author.name}</div>
              </div>

              {post.author.id === userId && (
                <div className="flex items-center gap-4 text-gray-600">
                  <EditIcon onClick={() => handleEdit(post)} className="cursor-pointer text-xl hover:text-blue-500">
                    <i className="fa-solid fa-pen-to-square"></i>
                  </EditIcon>
                  <DeleteIcon
                    onClick={() => handleDelete(post.id)}
                    className="cursor-pointer text-xl hover:text-red-500"
                  >
                    <i className="fa-solid fa-trash"></i>
                  </DeleteIcon>
                </div>
              )}
            </PostHeader>

            <PostBody className="mt-4">
              {editingPostId === post.id ? (
                <>
                  <InputStyled
                    value={editedTitle}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEditedTitle(e.target.value)}
                    className="w-full p-2 mb-4 border border-gray-300 rounded-md"
                  />
                  <TextareaStyled
                    value={editedContent}
                    onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => setEditedContent(e.target.value)}
                    className="w-full p-2 mb-4 border border-gray-300 rounded-md"
                  />
                </>
              ) : (
                <>
                  <PostTitle className="text-2xl font-bold text-gray-800">{post.title}</PostTitle>
                  <PostContent dangerouslySetInnerHTML={{ __html: purifier.sanitize(post.content) }} />
                </>
              )}

              <div className="mt-4">
                <div
                  className="flex items-center gap-2 cursor-pointer text-sm text-gray-600 font-semibold hover:text-blue-600"
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
                  <ul className="text-sm text-gray-800 pl-4 list-disc mt-1">
                    {(post.comments ?? []).map((comment, index) => (
                      <li key={index}>{comment.content}</li>
                    ))}
                  </ul>
                )}
              </div>
            </PostBody>

            <PostFooter className="flex justify-between items-center mt-4">
              {editingPostId === post.id ? (
                <SendEditIcon onClick={() => handleSubmitEdit(post.id)} className="text-blue-500 cursor-pointer">
                  <i className="fas fa-paper-plane" />
                </SendEditIcon>
              ) : (
                <>
                  <CommentBox
                    placeholder="Write a comment..."
                    value={commentInput[post.id] || ""}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                      setCommentInput((prev) => ({
                        ...prev,
                        [post.id]: e.target.value,
                      }))
                    }
                    className="w-full p-2 border border-gray-300 rounded-md"
                  />
                  <SendEditIcon onClick={() => handleCommentSubmit(post.id)} className="text-blue-500 cursor-pointer">
                    <i className="fas fa-paper-plane" />
                  </SendEditIcon>
                </>
              )}
            </PostFooter>
          </PostWrapper>
        ))}
    </div>
  );
}
