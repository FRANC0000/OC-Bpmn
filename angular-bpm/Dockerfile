FROM node:20-alpine AS build
WORKDIR /build
COPY package.json package-lock.json ./
RUN rm package-lock.json && npm install
COPY . .
RUN npm run build -- --configuration production

FROM nginx:alpine
COPY --from=build /build/dist/bpm-platform/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
