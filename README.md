# Проект SOAP Client на Spring Framework з підтримкою системи Трембіта

## Опис
Цей проєкт входить в набір прикладів взаємодії з API шлюзу безпечного обміну системи Трембіта (ШБО), що демонструють технічні особливості розробки вебсервісів та вебклієнтів, сумісних з системою Трембіта.
Даний проєкт представляє опис створення сумісного REST-клієнта на мові програмування Java з використанням фрейморка Spring. Spring - це сучасний, швидкий (високопродуктивний) фреймворк для Java.\
Приклад описує отримання з умовної бази даних (реєстру) відомостей про інформаційні об'єкти (у прикладі об'єктом є користувач) та управління їх статусом, у тому числі обробку запитів на пошук, отримання, створення, редагування та видалення об'єктів через REST API, що доступне через систему Трембіта.\
Проєкт демонструє:
- Cтандартні синхронні та асинхронні виклики
- Логування роботу застосунку з налаштуванням деталізації
- Роботу з файлом конфігурації
- Підтримку системи Трембіта (логування заголовків, отримання ASiC-контейнерів).

## Вимоги
- Java 17+
- Git (для клонування репозиторію)
- Ubuntu Server 22.04

## Встановлення

### Встановлення за допомогою скрипта install_SoapClient.sh

Було створено скрипт `install_SpringClient.sh` для автоматизації процесу встановлення середовища розробки, готового до компіляції прикладу. Скрипт виконує наступні кроки:
1. Встановлює системні залежності.
2. Клонує репозиторій.
3. Встановлює або оновлює Java 21.
4. Встановлює та налаштовує проєкт

#### Використання скрипта install_SoapClient.sh

На вашій робочій станції для розробки виконайте наступні кроки:
1. Завантажте скрипт автоматизациї встановлення `install_SpringClient.sh` в папку Documents:
   ```bash
   wget https://raw.githubusercontent.com/Wishmaster-sa/SpringClientSoap/master/install_SoapClient.sh
   ```

2. Зробіть файл виконуваним:

   ```bash
   sudo chmod +x install_SoapClient.sh
   ```

3. Запустіть скрипт:

   ```bash
   sudo bash ./install_SoapClient.sh
   ```
4. В деяких випадках скрипт може пропонувати перезавантажити деякі системні служби - просто натисніть Enter.

5. Відкрити файл конфігурації `./SpringClientSoap/config/config.properties`
   ```bash
   nano ./SpringClientSoap/config/config.properties
   ```
та встановити коректні параметри підключення до сервіса 

    * Вам треба вказати адрес сервіса (server-path)
    * порт клієнта (port)
    * Встановити або змінити файл логування (logfilename)
    * Встановити або змінити рівень логування. (0 - немає логування, 2 - є повне логування) 
    * У разі якщо ви бажаєте використовувати SSL то треба встановити:
    * Потреба використовувати SSL (ssl=true)
    * Шлях до файлів сертіфікатів (certs-path)
    * Шлях до сховища довіри (trust-store-path та trust-store-password)
    * Шлях до збереження ASIC контейнерів (asic-store)
    * Також у разі використовування сервіса разом Трембітою треба налаштувати хедери (headers)
    * 
    * Зберегите файл налаштувань та виконайте наступну команду:  bash start-client.sh
    * Кліент буде доступний за адресою http[s]://localhost:[port]
    * також у вас повинен бути вже розгорнутий та налаштований SpringWSrest сервіс до якого цей
    * клієнт буде звертатись.


6. Після завершення процесу налаштування запустить сервіс командою:
   ```bash
   bash start-client.sh
   ```
після того як сервіс запуститься ви зможете подивитись його по адресу: 

http://ваш_IP_адрес:8050/


### Ручне встановлення

#### Оновлення списку доступних пакетів програмного забезпечення з офіційних репозиторіїв 

```bash
sudo apt update
```

#### Встановлення Java JDK 21

```bash
sudo apt install openjdk-21-jdk 
```

#### Встановлення Maven

```bash
sudo apt install maven 
```

#### Завантажити Github клієнт

```bash
sudo apt install git
```
#### Клонування репозиторію

   ```bash
   git clone https://github.com/Wishmaster-sa/SpringClientSoap.git
   ```

#### Налаштування підключення до БД в проєкті

Відкрити файл конфігурації застосунку Spring `./SpringClientSoap/config/config.properties`
   ```bash
   nano ./SpringClientSoap/config/config.properties
   ```
та встановити коректні параметри підключення до сервіса 

```
server.port=8050
webclient.settings.logfilename=client.log
webclient.settings.loglevel=2
webclient.settings.server-path=https://192.168.99.93/restapi
webclient.settings.ssl=false
webclient.settings.certs-path=./certs
webclient.settings.asic-store=asic
webclient.settings.trust-store-path=keystore/keystore.p12
webclient.settings.trust-store-password=localhost
webclient.settings.headers={'UXP-CLIENT': 'test1/GOV/00000088/TEST_SUB888', 'UXP-SERVICE': 'test1/GOV/00000088/TEST_SUB888/springrest'}
```
    * Вам треба вказати адрес сервіса (server-path)
    * порт клієнта (port)
    * Встановити або змінити файл логування (logfilename)
    * Встановити або змінити рівень логування. (0 - немає логування, 2 - є повне логування) 
    * У разі якщо ви бажаєте використовувати SSL то треба встановити:
    * Потреба використовувати SSL (ssl=true)
    * Шлях до файлів сертіфікатів (certs-path)
    * Шлях до сховища довіри (trust-store-path та trust-store-password)
    * Шлях до збереження ASIC контейнерів (asic-store)
    * Також у разі використовування сервіса разом Трембітою треба налаштувати хедери (headers)
    * 
    * Зберегите файл налаштувань та виконайте наступну команду:  bash start-client.sh
    * Кліент буде доступний за адресою http[s]://localhost:[port]
    * також у вас повинен бути вже розгорнутий та налаштований SpringWSrest сервіс до якого цей
    * клієнт буде звертатись.




#### Переходимо до каталогу з проєктом

   ```bash
   cd SpringClientSoap
   ```

### Компілюємо вихідний файл
```bash
/usr/bin/mvn package
```

### Запуск клієнта (ви повинні знаходитись в каталозі з сервісом SpringClientSoap)

```bash
bash start-client.sh
```

Сервіс запуститься і буде очікувати на вхідні мережеві з'єднання на порту 8050 за адресою
http://localhost:8050/


## Документація

Додаткова документація до сервісу міститься в каталозі docs
- Інструкція по налаштуванню взаємного TLS-зʼєднання.

## Налаштування як системної служби systemd

Для цілей швидкого запуску сервісу ми додаємо скрипт `springwc-service.sh` для автоматизації процесу встановленя веб-додатка як системної служби, та його видалення та очищення системи.

### Використання скрипта springwc-service.sh
1. Скомпілюйте через середовище розробки та скопіюйте jar-файл веб-сервісу до цільової системи.
   Ви повинні отримати в папці SpringClientSoap/target/SpringClientSoap-0.0.1-SNAPSHOT.jar скомпільований jar-файл 

2. Зробіть скріпт виконуваним:

   ```bash
   chmod +x springwc-service.sh
   ```

3. Запустіть скрипт з каталогу SpringClientSoap:

   ```bash
   sudo bash springwc-service.sh install
   ```

Скрипт автоматично вcтановить скомпільований jar як системну службу.

Якщо ви бажаєте видалити сервіс з переліку системних служб, то виконайте команду
   ```bash
   sudo bash springwc-service.sh remove
   ```
Скрипт автоматично зупинить та видалить системну службу та відповідні залежності.

Запустити сервіс
   ```bash
   sudo bash springwc-service.sh start
   ```
Припинити сервіс
   ```bash
   sudo bash springwc-service.sh stop
   ```
Якщо ви виконаєте файл без аргументів командного рядку, то вам буде запропоновано меню з наступними опціями:
1) Встановити сервіс
2) Запустити сервіс
3) Припинити сервіс
4) Видалити сервіс
5) Вихід


## Ліцензія

Цей проєкт ліцензується відповідно до умов MIT License.
