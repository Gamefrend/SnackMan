# Stage 1: Verwende Node.js für den Entwicklungsmodus
FROM node:20 AS dev
WORKDIR /app

# Kopiere die package.json und installiere Abhängigkeiten
COPY package*.json ./
RUN npm install

# Kopiere den Rest des Codes
COPY . .

# Exponiere den Port 5173, der vom Vite-Entwicklungsserver verwendet wird
EXPOSE 5173

# Starte den Vite-Entwicklungsserver
CMD ["npm", "run", "dev"]
