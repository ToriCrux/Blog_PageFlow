// BarraEsquerda/useUserData.ts
"use client";

import { useEffect, useState } from "react";
import { BlogUser, fetchUserData } from "@/app/API/UserAPI/ApiUserData";

export function useUserData() {
  const [user, setUser] = useState<BlogUser | null>(null);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const loadUser = async () => {
      const storedToken = localStorage.getItem("token");
      setToken(storedToken);
      const data = await fetchUserData();
      setUser(data);
    };
    loadUser();
  }, []);

  return { user, token };
}
