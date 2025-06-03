import tw from "tailwind-styled-components";

export const PostWrapper = tw.div`
  bg-[#E9E9E9]
  rounded-md
  p-4
  my-6
  shadow-md
`;

export const PostHeader = tw.div`
  flex
  items-center
  gap-4
  mb-2
`;

export const AuthorImage = tw.div`
  w-8
  h-8
  rounded-full
  bg-gray-400
`;

export const PostBody = tw.div`
  mb-4
`;

export const PostTitle = tw.h2`
  text-xl
  font-semibold
  text-[#1e1e1e]
  mb-1
`;

export const PostContent = tw.div`
  text-sm
  text-gray-700
  [&_strong]:font-bold
  [&_em]:italic
  [&_ul]:list-disc
  [&_ul]:pl-6
  [&_ol]:list-decimal
  [&_ol]:pl-6
  [&_a]:text-blue-600
  [&_a]:underline
`;

export const PostFooter = tw.div`
  flex
  items-center
  gap-2
`;

export const CommentBox = tw.input`
  flex-1
  rounded-md
  px-3
  py-2
  bg-[#d6d6d6]
  text-sm
  placeholder-gray-500
  outline-none
`;

export const DeleteIcon = tw.div`
  ml-auto
  text-[#9C0D38]
  cursor-pointer
  text-lg
  font-bold
  hover:text-red-700
`;

export const EditIcon = tw.div`
  text-red-500
  cursor-pointer
  text-lg
  ml-2
  hover:text-red-700
`;

export const SendEditIcon = tw.div`
  text-[#9C0D38]
  text-xl
  cursor-pointer
  ml-auto
`;

export const InputStyled = tw.input`
  w-full
  bg-white
  border
  border-gray-300
  rounded-md
  px-3
  py-1
  text-sm
  mb-2
`;

export const TextareaStyled = tw.textarea`
  w-full
  bg-white
  border
  border-gray-300
  rounded-md
  px-3
  py-1
  text-sm
  resize-none
`;


