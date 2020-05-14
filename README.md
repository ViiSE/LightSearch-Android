<p align="center"> 
<img src="https://user-images.githubusercontent.com/43209824/64838878-905c6e00-d638-11e9-8026-e7b04d1af80f.png"
     width="300" height="300">
</p>

LightSearch Android
===================

LightSearch Android - Мобильное клиентское приложение на операционной системе Android.

Принцип работы
--------------
Архитектура приложения построена на Android Fragment'ах и одном управляющем Activity. Для взаимодействия с LightSearch Server используется библиотека Retrofit2.

Список основных Fragment'ов:
1) Авторизация
2) Поиск
3) Результат поиска
4) Задачи
5) Привязка
6) Результат привязки
7) Отвязка
8) Открытие мягкого чека
9) Мягкий чек
10) Корзина
11) Еще
12) Документация

Функции
-------
1) Поиск товара по:
   - Наименованию, части наименования 
   - Короткому коду 
   - Штрих-коду (ввод и считывание через камеру смартфона) 
   - Подразделению (определенный склад/все склады, определенный ТК/все ТК, или все поздразделения)
2) Привязка товара
3) Отвязка товара
4) Открытие, закрытие, отмена мягкого чека
5) Документация

Зашифрованная авторизация
-------------------------
LightSearch Android поддерживает зашифрованную авторизацию через точку POST [/clients/login/encrypted](https://github.com/ViiSE/LightSearch-Server/blob/master/README.md#%D0%B7%D0%B0%D1%88%D0%B8%D1%84%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D0%B0%D1%8F-%D0%B0%D0%B2%D1%82%D0%BE%D1%80%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F). 

Ссылки
------
История проекта LightSearch доступна в [документах](https://github.com/ViiSE/LightSearch/tree/master/Documents/Project%20history)
и в [заметках разработки](https://github.com/ViiSE/LightSearch/blob/master/Dev%20notes).

Используемые библиотеки и технологии
------------------------------------
- [spots-dialog](https://github.com/d-max/spots-dialog)
- [ZXing](https://github.com/zxing/zxing)
- [AppUpdater](https://github.com/javiersantos/AppUpdater)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://github.com/mockito/mockito)
- [CounterFab](https://github.com/andremion/CounterFab)
- [Retrofit2](https://github.com/square/retrofit)
- [JWTDecode.Android](https://github.com/auth0/JWTDecode.Android)
- [Progress Button Android](https://github.com/leandroBorgesFerreira/LoadingButtonAndroid)
- [Google Truth](https://github.com/google/truth)
