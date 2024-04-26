FROM node:alpine AS builder

WORKDIR /app

COPY . /app

RUN npm install
RUN npm install -g @angular/cli
EXPOSE 4200
CMD ["ng", "serve", "--configuration", "development", "--host", "0.0.0.0"]
