# Инструкция по запуску проекта (для пользователей Windows)

- Скачать и установить Docker Dekstop for Windows
- После в терминале запустить команду `docker network create cake`
- Клонировать проект и открыт в Intellij IDEA
- В терминале в главной папке проекте (../cake-web/) запустить команду `mvn clean install`
- После запустить команду `docker build -f .\Dockerfile -t cake-pake .`
- После запустить команду `docker-compose -f .\docker-compose.yml up -d`

## Сайт
- Главная страница `http://localhost:8080/cake-pake-latest/ProductList`
- Админ: пользователь `admin` пароль `admin`
- Магазин 1: пользователь `kulikov` пароль `test`
- Магазин 2: пользователь `qulpinay` пароль `test`
- Клиент: пользователь `beka` пароль `test`
- Чтобы псомотреть лог в терминале запустить команду `docker logs -f -t web`

## База
- pgAdmin `http://localhost:5050/`
- [Вести пароль `admin` для pgAdmin](https://skr.sh/sEl226MHH6T?a)
- [Добавить новый сервер](https://skr.sh/sElZq5L0tgF?a)
- [Название сервера на усмотрение](https://skr.sh/sElEq2bSKDZ?a)
- [Host: `db`, port: `5432`, database: `cake`, username/password: `postgres`](https://skr.sh/sElcMRj4Knc?a)
- [Таблицы в схеме `web`](https://skr.sh/sElEzLrJEAk?a)
- Чтобы псомотреть лог в терминале запустить команду `docker logs -f -t db`
