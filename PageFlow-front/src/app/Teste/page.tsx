import Image from "next/image";
import { TesteDiv } from "./styles";

export default function Teste() {
  return (
    <TesteDiv>
      <Image 
        src="/Em_Construcao.svg" 
        alt="Building Illustration"
        width={600}
        height={600}
        style={{ width: "20%", height: "auto" }}
        priority
      />
    </TesteDiv>
  );
}
