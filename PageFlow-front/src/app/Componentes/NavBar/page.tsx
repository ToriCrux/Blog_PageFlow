'use client';

import Image from 'next/image';
import "@fortawesome/fontawesome-free/css/all.min.css";
import { Container, LogoArea, IconGroup, IconButton } from "./styles";

import { poppins } from "../../fonts";

export default function NavBar() {
  return (
    <div className={poppins.className}>
      <Container>
        <LogoArea>
          <Image src="/planeta_logo.svg" alt="Logo" width={25} height={25} />
          <Image 
            src="/titulo_logo.svg" 
            alt="Logo" 
            width={120} 
            height={35} 
            style={{ marginLeft: "8px" }}
          />
        </LogoArea>


        <IconGroup>
          <IconButton>
            <i className="fas fa-bell" />
          </IconButton>
          <IconButton>
            <i className="fas fa-moon" />
          </IconButton>
        </IconGroup>
      </Container>
    </div>
  );
}
