export interface FormularioCadastro {
    name: string;
    username: string;
    email: string;
    password: string;
    confirmPassword: string;
    role: string;
  }
  
  export interface ResultadoValidacao {
    valido: boolean;
    mensagens: string[];
  }
  
  export function validarFormulario(form: FormularioCadastro): ResultadoValidacao {
    const mensagens: string[] = [];
  
    if (!form.name.trim()) mensagens.push("O campo Nome é obrigatório.");
    if (!form.username.trim()) mensagens.push("O campo Username é obrigatório.");
    if (!form.email.trim()) mensagens.push("O campo Email é obrigatório.");
    if (!form.password.trim()) mensagens.push("O campo Senha é obrigatório.");
    if (!form.confirmPassword.trim()) mensagens.push("O campo Confirmar Senha é obrigatório.");
    if (!form.role.trim()) mensagens.push("O campo Perfil é obrigatório.");
  
    if (form.password !== form.confirmPassword) {
      mensagens.push("As senhas não coincidem.");
    }
  
    return {
      valido: mensagens.length === 0,
      mensagens,
    };
  }
  