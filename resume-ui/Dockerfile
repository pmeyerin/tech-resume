FROM node:alpine AS builder

WORKDIR /app

COPY . /app

RUN npm install -g @angular/cli
CMD ["ng", "serve", "--configuration", "solo", "--host", "0.0.0.0"]
