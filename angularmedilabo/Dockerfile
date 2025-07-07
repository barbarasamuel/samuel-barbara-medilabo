# Étape 1 : builder l'application Angular
FROM node:20-alpine as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
#RUN npm run build --prod
RUN npm run build -- --configuration production

# Étape 2 : NGINX pour servir les fichiers
FROM nginx:alpine

# Copie des fichiers compilés vers NGINX
COPY --from=build /app/dist/medilabo/browser /usr/share/nginx/html

# Copie d’un fichier de config nginx personnalisé si besoin
COPY default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]