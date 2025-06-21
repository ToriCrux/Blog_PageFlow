import tw from 'tailwind-styled-components';


export const Container = tw.div`
  fixed
  top-0
  left-0
  right-0
  z-50            
  h-16
  bg-[#9C0D38]
  flex
  items-center
  justify-between
  px-6
  shadow-md
`;


export const LogoArea = tw.div`
  flex
  items-center
`;

export const IconGroup = tw.div`
  flex
  items-center
  gap-4
`;

export const IconButton = tw.button`
  text-white
  text-xl
  hover:text-gray-300
  transition-colors
`;

export const BarraSuperior = tw.div`
  bg-[#2F2E41]
  dark:bg-[#1e1e2f]
  h-[150px]
  pl-16            
  mt-16          
  flex
  items-end
  font-poppins
`;