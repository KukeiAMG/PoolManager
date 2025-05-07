# PoolManager

## Описание
Реализовать REST API для организации работы бассейна (регистрация клиентов, работа с записями на посещение).

## Запуск

## API Endpoints

### Clients
Метод	Путь	Описание
1. GET  /api/v0/pool/client/all  (Получить всех клиентов)

2. GET /api/v0/pool/client/get/{id}  (Получить конкретного клиента по id)
Входные данные
    id: number

3. POST /api/v0/pool/client/add  (Создать клиента)
Структура входных данных (body) 
{
    "name": string,
    "phone": string,
    "email": string
}    

4. POST /api/v0/pool/client/update (обновить коиента)
Структура входных данных (body) 
{
    "id": number
    "name": string,
    "phone": string,
    "email": string
} 
