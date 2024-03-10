## Define node version
#FROM node:latest as build
## Define container directory
#WORKDIR .
## Copy package*.json for npm install
#COPY package*.json ./
## Run npm clean install, including dev dependencies for @angular-devkit
#RUN npm ci
## Run npm install @angular/cli
#RUN npm install -g @angular/cli
## Copy all files
#COPY . .
## Run ng build through npm to create dist folder
#RUN npm run build --prod
## Define nginx for front-end server
#FROM nginx:latest
## Copy dist from ng build to nginx html folder
#COPY --from=build /usr/src/app/dist /usr/share/nginx/html

FROM node:alpine AS builder

WORKDIR /app

COPY . /app

RUN npm install
RUN npm run build

FROM nginx:alpine

COPY --from=builder /app/dist/* /usr/share/nginx/html/
COPY /nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
