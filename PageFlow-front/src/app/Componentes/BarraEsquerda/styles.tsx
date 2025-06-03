import tw from "tailwind-styled-components";

interface ContainerProps {
  $expanded: boolean;
}

export const Container = tw.div<ContainerProps>`  
  fixed
  top-16       
  left-0
  z-40
  ${(p) => (p.$expanded ? "w-64" : "w-16")}
  h-[calc(100vh-4rem)]  
  bg-[#E9E9E9]
  flex
  flex-col
  justify-between
  transition-all
  duration-300
  shadow-md
`;


export const MenuWrapper = tw.div`
  flex
  flex-col
  gap-2
  mt-4
  px-2
`;

export const IconWrapper = tw.div`
  text-black
  text-xl
  w-6
  flex
  justify-center
`;

export const MenuItem = tw.div<{ $selected?: boolean }>`
  flex
  items-center
  gap-4
  h-12
  w-full
  px-3
  rounded-md
  cursor-pointer
  transition-all
  duration-200
  ${(p) =>
    p.$selected
      ? "bg-[#9C0D38BF] text-white"
      : "hover:bg-[#9C0D38] hover:text-white"}
`;

export const IconLabel = tw.span`
  text-black
  font-medium
  transition-opacity
  duration-300
`;

export const Footer = tw.div`
  flex
  flex-col
  gap-2
  px-2
  mb-4
`;

export const UserInfo = tw.div`
  flex
  items-center
  gap-4
  p-4
  bg-[#9C0D38]
  text-white
  rounded-md
`;

export const UserName = tw.div`
  font-bold
  text-lg
`;

export const UserStatus = tw.div`
  text-sm
  text-green-300
`;
