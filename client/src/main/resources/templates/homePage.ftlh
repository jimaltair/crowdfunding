<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Home page </title>
</head>
<body>
<h1>СПИСОК ПРОЕКТОВ КОТОРЫЕ CONFIRMED</h1>

    <div id="main-content" class="container">
        <div class="row">
            <div class="col-md-6">
                <div class="form-inline">
                    <div class="form-group">
                        <label for="Hello">Главная страница: </label>
                        <button id="signUp"  type="submit" onclick="location.href = 'http://localhost:8080/signUp'"
                        >Зарегистрироваться</button>
                        <button id="signIn" class="btn btn-default" type="submit"
                                onclick="location.href = 'http://localhost:8080/signIn'">Войти
                        </button>
                        <button id="logout" class="btn btn-default" type="submit"
                                onclick="location.href = 'http://localhost:8080/logout'">Выход
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <#list ListProject as projects>
            <p><a href="localhost:8080/projects/${projects.id}">Проект: ${projects.getTitle()}</a></p>
            </#list>

 </body>
</html>