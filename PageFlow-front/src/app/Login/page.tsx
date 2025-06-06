// Login/Login.tsx
"use client";

import Link from "next/link";
import Image from "next/image";
import "@fortawesome/fontawesome-free/css/all.min.css";
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
  LoginImage,
} from "./styles";

import { useLogin } from "./useLogin";

export default function Login() {
  const { form, handleChange, handleSubmit } = useLogin();

  return (
    <Container>
      <LeftSection>
        <LoginImage>
          <Image
            src="/Login.svg"
            alt="Login Illustration"
            width={600}
            height={600}
            style={{ width: "100%", height: "auto" }}
            priority
          />
        </LoginImage>
        <LogoContainer>
          <Image
            src="/Logo.svg"
            alt="Page Flow Logo"
            width={600}
            height={50}
            style={{ width: "100%", height: "auto" }}
          />
        </LogoContainer>
        <Phrase>Escreva. Compartilhe. Conecte-se.</Phrase>
      </LeftSection>

      <RightSection>
        <SignInContainer>
          <TitleGroup>
            <Title>Sign In</Title>
            <Subtitle>Sign in with Open Account</Subtitle>
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
          <label className="text-xl">Email address</label>
          <InputContainer>
            <InputIcon>
              <i className="fa-solid fa-envelope"></i>
            </InputIcon>
            <InputField type="email" name="email" placeholder="Email ..." value={form.email} onChange={handleChange} />
          </InputContainer>

          <label className="text-xl mt-4">Password</label>
          <InputContainer>
            <InputIcon>
              <i className="fa-solid fa-lock"></i>
            </InputIcon>
            <InputField
              type="password"
              name="password"
              placeholder="Password ..."
              value={form.password}
              onChange={handleChange}
            />
          </InputContainer>

          <SignInButton onClick={handleSubmit}>Sign In</SignInButton>

          <RegisterText>
            Don&apos;t have an account?
            <Link href="/Cadastro">
              <span className="text-blue-900"> Create one</span>
            </Link>
          </RegisterText>
        </InputGroup>
      </RightSection>
    </Container>
  );
}
