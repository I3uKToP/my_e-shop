<h1>Интернет магазин</h1>
<p>Проект модульный, собран на Maven. На текущий момент используется 4 модуля:</p>
<ul>
 <li>Модуль для администрирования сайта. CMS. Добавляем категории, продукты, пользователей. Front-end с использованием Theamleaf</li>
 <li>Модуль для rest api для пользовательской части</li>
 <li>Модуль для базы данных</li>
 <li>Front-end для пользователя на Angular</li>
</ul>
<h3>CMS. Модуль для администрирования</h1>
<p>Написана с использованием технологий Spring boot,  Sping MVC, Spring Security </p>
<p>В серверную часть добавляются пользователи, редактируются возможные продукты и картинки к продуктам, категори, распределяются роли для администрирования и для контент менеджера</p>

<h3>Модуль для rest api для пользовательской части</h1>
<p>Написан с использованием технологий Spring boot,  Sping MVC, Spring Security </p>
<p>Данный модуль предназачен для отправки информации с сервера на сторону клиената в формате JSON </p>

<h3>Модуль для базы данных</h1>
<p>Написан с использованием Spring boot JPA и Hibernate </p>
<p>Модуль презназачен для хранения всей информации на сервере, он покдлючается к модулю администрирования и resp api </p>

<h3>Модуль для Front-end</h1>
<p>Написан с использованием Angular CLI и TypeScript </p>
<p>Модуль предназачен для клиентской части приложения </p>
