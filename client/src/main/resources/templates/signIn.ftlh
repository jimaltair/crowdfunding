<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>SignIn</title>
</head>
<body>
<h1>Авторизуйтесь, чтобы продолжить</h1>
<h2>Введите свой email и пароль</h2>
<form action="/signIn" method="post">
    <label for="email">Адрес электронной почты:</label>
    <input type="email" id="email" name="email" placeholder="Your email">
    <br>
    <br>
    <label for="password">Ваш пароль:</label>
    <input type="password" id="password" name="password" placeholder="Your password">
    <br>
    <br>
    <input type="submit" value="Войти">
</form>
</body>
</html>
