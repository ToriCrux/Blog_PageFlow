import tw from "tailwind-styled-components";

export const Container = tw.div`
    flex
    flex-col
    xl:flex-row 
    h-screen
    w-full
    font-poppins
`;

export const LogoContainer = tw.div`
  mt-4
`;

export const LoginImage = tw.div`
  w-3/4
  xl:w-[600px]
  max-w-[90%]
`;

export const LeftSection = tw.div`
  w-full
  xl:w-1/2
  h-auto
  xl:h-full
  bg-[#9C0D38]
  flex
  flex-col
  items-center
  justify-center
  text-white
  py-10
`;

export const Phrase = tw.p`
  text-white
  text-xl
  xl:text-4xl
  font-medium
  font-montserrat
`;

export const RightSection = tw.div`
    w-full
    xl:w-1/2
    h-auto
    xl:h-full
    bg-white
    flex
    flex-col
    items-center
    justify-center
    xl:py-10
    xl:px-6
`;

export const TitleGroup = tw.div`
    space-y-5
`;

export const SignInContainer = tw.div`
  flex
  flex-col
  w-[80%]
  mt-9
  xl:mt-0
`;

export const Title = tw.h1`
    text-6xl
    font-bold
    text-black
`;

export const Subtitle = tw.p`
    text-2xl
    text-gray-600
    mt-2
`;

export const ButtonGroup = tw.div`
  flex
  mt-6
  space-x-4
`;

export const Button = tw.button`
  flex-1
  text-xl
  bg-gray-200
  p-3
  rounded-lg
  text-black
  font-medium
`;


export const Divider = tw.div`
  flex
  items-center
  justify-center
  w-full
  my-6
  relative
`;

export const Line = tw.hr`
  flex-grow
  border-t-2
  border-[#9C0D38]
  mx-4
`;

export const OrText = tw.span`
  bg-white
  px-2
  text-[#9C0D38]
  font-medium
  text-sm
  absolute
  left-1/2
  transform -translate-x-1/2
`;



export const InputGroup = tw.div`
  flex
  flex-col
  w-[80%]
  mt-6
`;

export const InputContainer = tw.div`
  flex
  items-center
  bg-gray-200
  p-3
  rounded-lg
  mt-3
`;

export const InputIcon = tw.span`
  text-lg
  mr-3
`;

export const InputField = tw.input`
  flex-1
  bg-transparent
  border-none
  outline-none
  text-black
  placeholder-gray-500
`;


export const SignInButton = tw.button`
  w-full
  bg-blue-600
  text-white
  font-bold
  p-3
  rounded-lg
  mt-6
  hover:bg-blue-700
  transition
  duration-300
`;

export const RegisterText = tw.p`
  text-gray-600
  text-center
  mt-4
  text-lg
`;

export const RegisterTextSpan = tw.span`
  text-blue-600
  cursor-pointer
  font-semibold
  ml-2
  hover:underline
`;
