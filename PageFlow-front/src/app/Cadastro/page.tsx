'use client';

import Link from "next/link";
import Image from "next/image";
import { useState } from "react";
import { cadastrarUsuario } from "./apiService";
import { validarFormulario } from "./validarFormulario";

import {
  Container,
  LeftSection,
  RightSection,
  Phrase,
  SignInContainer,
  Title,
  Subtitle,
  ButtonGroup,
  Button,
  Divider,
  Line,
  OrText,
  TitleGroup,
  InputGroup,
  InputContainer,
  InputIcon,
  InputField,
  SignInButton,
  RegisterText,
  LogoContainer,
  LoginImage
} from "./styles";

import "@fortawesome/fontawesome-free/css/all.min.css";
import { Montserrat, Poppins } from "next/font/google";

 const montserrat = Montserrat({ subsets: ["latin"], weight: ["400", "700"] });
 const poppins = Poppins({ subsets: ["latin"], weight: ["400", "700"] });

export default function Cadastro() {
  const [form, setForm] = useState({
    name: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: "AUTHOR"
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    const resultado = validarFormulario(form);

    if (!resultado.valido) {
      alert(resultado.mensagens.join("\n"));
      return;
    }

    try {
      await cadastrarUsuario({
        name: form.name,
        username: form.username,
        email: form.email,
        password: form.password,
        role: form.role
      });

      alert("Usuário cadastrado com sucesso!");
      setForm({
        name: "",
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
        role: "ADMINISTRATOR"
      });
    } catch (error: any) {
      alert("Erro ao cadastrar usuário: " + error.message);
    }
  };

  return (
    <Container>
      <LeftSection>
        <LoginImage>
          <Image 
            src="/Cadastro.svg" 
            alt="Login Illustration"
            layout="responsive"
            width={600}
            height={600}
          />
        </LoginImage>

        <LogoContainer>
          <Image 
            src="/Logo.svg" 
            alt="Page Flow Logo"
            width={600}
            height={50}
          />
        </LogoContainer>

        <Phrase>Escreva. Compartilhe. Conecte-se.</Phrase>
      </LeftSection>
      
      <RightSection>
        <SignInContainer>
          <TitleGroup>
            <Title>Create your account</Title>
            <Subtitle>Let’s get start!</Subtitle>
          </TitleGroup>

          <ButtonGroup>
            <Button className="google">
              <i className="fab fa-google mr-2"></i> Continue with Google
            </Button>
            <Button className="facebook">
              <i className="fab fa-facebook-f mr-2"></i> Continue with Facebook
            </Button>
          </ButtonGroup>

          <Divider>
            <Line />
            <OrText>or</OrText>
            <Line />
          </Divider>
        </SignInContainer>
        
        <InputGroup>
          <label className="text-xl">Name</label>
          <InputContainer>
            <InputIcon>👤</InputIcon>
            <InputField 
              type="text" 
              name="name" 
              placeholder="Name ..."
              value={form.name}
              onChange={handleChange}
            />
          </InputContainer>

          <label className="text-xl mt-4">Username</label>
          <InputContainer>
            <InputIcon>👥</InputIcon>
            <InputField 
              type="text" 
              name="username" 
              placeholder="Username ..."
              value={form.username}
              onChange={handleChange}
            />
          </InputContainer>

          <label className="text-xl mt-4">Email address</label>
          <InputContainer>
            <InputIcon>📩</InputIcon>
            <InputField 
              type="email" 
              name="email" 
              placeholder="Email ..."
              value={form.email}
              onChange={handleChange}
            />
          </InputContainer>

          <label className="text-xl mt-4">Password</label>
          <div className="flex flex-col xl:flex-row gap-4 w-full">
            <InputContainer className="w-full xl:w-1/2">
              <InputIcon>🔒</InputIcon>
              <InputField 
                type="password" 
                name="password" 
                placeholder="Password ..."
                value={form.password}
                onChange={handleChange}
              />
            </InputContainer>

            <InputContainer className="w-full xl:w-1/2">
              <InputIcon>🔒</InputIcon>
              <InputField 
                type="password" 
                name="confirmPassword" 
                placeholder="Confirm Password ..."
                value={form.confirmPassword}
                onChange={handleChange}
              />
            </InputContainer>
          </div>

          <SignInButton onClick={handleSubmit}>Sign Up</SignInButton>

          <RegisterText>
            Already have an account? 
            <Link href="/Login">
              <span className="text-blue-900"> Login in</span>
            </Link>
          </RegisterText>
        </InputGroup>
      </RightSection>
    </Container>
  );
}
