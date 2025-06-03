import tw from "tailwind-styled-components";

export const PostContainer = tw.div`
  bg-[#E9E9E9]
  rounded-md
  p-4
  mt-6
  
  w-full
  shadow-md
`;

export const TextArea = tw.textarea`
  w-full
  bg-transparent
  resize-none
  outline-none
  text-gray-700
  placeholder-gray-500
`;

export const Divider = tw.hr`
  border-t
  border-[#9C0D38]
  my-4
`;

export const ActionsRow = tw.div`
  flex
  justify-between
  items-center
`;

export const ActionButton = tw.button`
  bg-gray-300
  text-[#9C0D38]
  px-4
  py-2
  rounded-full
  flex
  items-center
  text-sm
`;

export const SendButton = tw.button`
  text-[#9C0D38]
  text-xl
`;
