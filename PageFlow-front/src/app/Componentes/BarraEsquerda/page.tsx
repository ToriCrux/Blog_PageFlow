"use client";

import { useState, useEffect } from "react";
import Image from "next/image";
import { usePathname, useRouter } from "next/navigation";
import { useUserData } from "./useUserData";
import { MenuItemWithIcon } from "./MenuItemWithIcon";
import { getAllPosts } from "../../API/Posts/GetPosts/GetPostsAPI";

import {
  Container,
  MenuWrapper,
  UserInfo,
  UserName,
  UserStatus,
  Footer
} from "./styles";

import { poppins } from "../../fonts";

export default function BarraEsquerda() {
  const [expanded, setExpanded] = useState(false);
  const [userPostCount, setUserPostCount] = useState<number>(0);
  const { user, token } = useUserData();
  const router = useRouter();
  const pathname = usePathname();

  const handleGoTo = (path: string) => router.push(path);

  useEffect(() => {
    const fetchUserPostCount = async () => {
      if (!user?.id) return;

      const allPosts = await getAllPosts();
      if (allPosts) {
        const count = allPosts.filter((post) => post.author.id === user.id).length;
        setUserPostCount(count);
      }
    };

    fetchUserPostCount();
  }, [user]);

  return (
    <div className={poppins.className}>
      <Container
        onMouseEnter={() => setExpanded(true)}
        onMouseLeave={() => setExpanded(false)}
        $expanded={expanded}
      >
        <MenuWrapper>
          {expanded && user && (
            <UserInfo>
              <Image src="/Perfil.svg" alt="User" width={50} height={50} />
              <div>
                <UserName>{user.name}</UserName>
                {token ? (
                  <UserStatus>
                    🟢 Ativo <span style={{ color: "white" }}>&nbsp;•&nbsp; {userPostCount} post{userPostCount !== 1 ? "s" : ""}</span>
                  </UserStatus>
                ) : (
                  <UserStatus>🔴 Offline</UserStatus>
                )}
              </div>
            </UserInfo>
          )}

          <MenuItemWithIcon
            icon="fas fa-home"
            label="Home"
            expanded={expanded}
            selected={pathname === "/Home"}
            onClick={() => handleGoTo("/Home")}
          />
          <MenuItemWithIcon
            icon="fas fa-search"
            label="Search"
            expanded={expanded}
            onClick={() => handleGoTo("/Search")}
          />
          <MenuItemWithIcon
            icon="fas fa-layer-group"
            label="Categories"
            expanded={expanded}
            onClick={() => handleGoTo("/Categorias")}
          />
          <MenuItemWithIcon
            icon="fas fa-user"
            label="User"
            expanded={expanded}
            selected={pathname === "/User"}
            onClick={() => handleGoTo("/User")}
          />
        </MenuWrapper>

        <Footer>
          <MenuItemWithIcon
            icon="fas fa-cog"
            label="Settings"
            expanded={expanded}
            onClick={() => {}}
          />
          <MenuItemWithIcon
            icon="fas fa-exclamation-circle"
            label="Support"
            expanded={expanded}
            onClick={() => {}}
          />
        </Footer>
      </Container>
    </div>
  );
}
