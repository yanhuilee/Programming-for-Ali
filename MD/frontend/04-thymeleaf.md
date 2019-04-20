```
<html xmlns:th="http://www.thymeleaf.org">

<span th:text="${book.author.name}">
<li th:each="book : ${books}">

```
<a th:href="@{/order/list}">...</a>
<li th:each="book : ${books}" th:text="${book.title}">En las Orillas del Sar</li>
