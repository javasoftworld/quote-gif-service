[![CircleCI](https://circleci.com/gh/javasoftworld/quote-gif-service.svg?style=shield)](https://circleci.com/gh/javasoftworld/quote-gif-service) 
# Gif-Service

## Небольшое тестовое задание, условия ниже.

### Создать сервис, который обращается к сервису курсов валют, и отдает gif в ответ:
- если курс по отношению к рублю за сегодня стал выше вчерашнего, то отдаем рандомную отсюда https://giphy.com/search/rich
- если ниже - отсюда https://giphy.com/search/broke

### Ссылки
- REST API курсов валют - https://docs.openexchangerates.org/
- REST API гифок - https://developers.giphy.com/docs/api#quick-start-guide

### Must Have
- Сервис на Spring Boot 2 + Java / Kotlin
- Запросы приходят на HTTP endpoint, туда передается код валюты
- Для взаимодействия с внешними сервисами используется Feign
- Все параметры (валюта по отношению к которой смотрится курс, адреса внешних сервисов и т.д.) вынесены в настройки
- На сервис написаны тесты (для мока внешних сервисов можно использовать @mockbean или WireMock)
- Для сборки должен использоваться Gradle
- Результатом выполнения должен быть репо на GitHub с инструкцией по запуску

### Nice to Have
- Сборка и запуск Docker контейнера с этим сервисом

## Как скачать, настроить и запустить из исходников

Запуск на Windows:
```
git clone https://github.com/javasoftworld/quote-gif-service.git
cd quote-gif-service
gradlew clean build
java -jar build\libs\exchange-gif-service-1.0.jar
```
REST-сервис будет доступен по адресу: <http://localhost:8080/api/v1/compare/{currency}>
- где {currency} - трехбуквенный код валюты

Например, для сравнения RUB и USD ссылка будет иметь такой вид: <http://localhost:8080/api/v1/compare/USD>

Настройки доступа к внешним сервисам вынесены в файл настроек *application.properties*

По умолчанию в качестве базовой валюты для сравнения в настройках установлен RUB.

При необходимости замены базовой валюты сравнения используйте параметр *api.open-exchange-rates.base* установив ISO-4217 совместимый трехбуквенный код валюты, например: EUR, GBP. 

Запуск на Windows:
```
java -jar build\libs\exchange-gif-service-1.0.jar --api.open-exchange-rates.base=THB
```
## Запуск из Docker

```
docker run --name gif-service -p 8080:8080 --detach sergeybelonosov/gif-service:latest
```


### Nice to play! :-)













