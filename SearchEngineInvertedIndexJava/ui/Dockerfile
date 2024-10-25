# Użyj obrazu Node.js jako podstawowego
FROM node:20-alpine AS build

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj package.json i package-lock.json
COPY package*.json ./

# Zainstaluj zależności
RUN npm install

# Skopiuj resztę kodu źródłowego
COPY . .

# Zbuduj aplikację
RUN npm run build

# Użyj serwera Nginx do serwowania aplikacji
FROM nginx:alpine

# Skopiuj zbudowane pliki do katalogu serwera Nginx
COPY --from=build /app/build /usr/share/nginx/html

# Skonfiguruj Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Otwórz port 80
EXPOSE 80

# Uruchom Nginx
CMD ["nginx", "-g", "daemon off;"]
