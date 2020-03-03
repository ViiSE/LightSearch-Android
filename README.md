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
4) Привязка
5) Результат привязки
6) Отвязка
7) Мягкий чек
8) Корзина

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

Используемые библиотеки и технологии
------------------------------------
- [spots-dialog](https://github.com/d-max/spots-dialog)
- [ZXing](https://github.com/zxing/zxing)
- [AppUpdater](https://github.com/javiersantos/AppUpdater)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://github.com/mockito/mockito)
