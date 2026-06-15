# syntax=docker/dockerfile:1
FROM node:22-alpine AS build

WORKDIR /build

COPY package.json package-lock.json ./
RUN --mount=type=cache,target=/root/.npm \
    npm ci

COPY . .
RUN --mount=type=cache,target=/root/.npm \
    npx ng build

FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

COPY --from=build /build/dist/bpm-platform /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
