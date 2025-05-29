FROM node:22-alpine AS base

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia package.json e package-lock.json (ou yarn.lock)
COPY package.json ./

# Instala as dependências
RUN npm install --frozen-lockfile

COPY . .

RUN npm run build

FROM node:22-alpine AS runner

WORKDIR /app

COPY --from=base /app/node_modules ./node_modules
COPY --from=base /app/.next ./.next
COPY --from=base /app/public ./public
COPY --from=base /app/package.json ./package.json

EXPOSE 3000

CMD ["npm", "start"]