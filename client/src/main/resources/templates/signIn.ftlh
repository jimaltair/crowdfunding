<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>SignIn</title>
    <style>
        .error {
            color: red;
        }
    </style>
</head>
<body>
<h1>Авторизуйтесь, чтобы продолжить</h1>
<h2>Введите свой email и пароль</h2>
<form action="/signIn" method="post">
    <@spring.bind path="authSignInRequest"/>
    <p class="error">${spring.status.errorMessage}</p>
    <label for="email">Адрес электронной почты:</label>
    <@spring.formInput 'authSignInRequest.email' 'id="email" placeholder="Емейл"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <label for="password">Ваш пароль:</label>
    <@spring.formInput 'authSignInRequest.password' 'id="password" class="text" placeholder="Пароль"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <input type="submit" value="Войти">
</form>
</body>
</html>
