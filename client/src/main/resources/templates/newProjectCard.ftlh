<#import "spring.ftl" as spring/>
<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>${project.title}</title>
</head>
<body>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css"/>
<link rel="stylesheet" href="/bootstrap/css/font-awesome.min.css"/>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<style>
    body {
        background-image: url("/img/bg-1.png");
    }

    #main {
        background-color: #f2f2f2;
        padding: 20px;
        -webkit-border-radius: 4px;
        -moz-border-radius: 4px;
        -ms-border-radius: 4px;
        -o-border-radius: 4px;
        border-radius: 4px;
        border-bottom: 4px solid #ddd;
    }

    #real-estates-detail #author img {
        -webkit-border-radius: 100%;
        -moz-border-radius: 100%;
        -ms-border-radius: 100%;
        -o-border-radius: 100%;
        border-radius: 100%;
        border: 5px solid #ecf0f1;
        margin-bottom: 10px;
    }

    #real-estates-detail .sosmed-author i.fa {
        width: 30px;
        height: 30px;
        border: 2px solid #bdc3c7;
        color: #bdc3c7;
        padding-top: 6px;
        margin-top: 10px;
    }

    .panel-default .panel-heading {
        background-color: #fff;
    }

    #real-estates-detail .slides li img {
        height: 450px;
    }
</style>

<!-- Navigation bar -->
<nav class="navbar navbar-dark bg-dark navbar-static-top navbar-expand-sm">
    <!-- Links -->
    <ul class="navbar-nav mr-auto">
        <li class="nav-item"><a class="nav-link" href="/">Главная</a></li>
        <#if userId == 0 >
            <li class="nav-item"><a class="nav-link" href="/signIn">Профиль</a></li>
            <#else><li class="nav-item"><a class="nav-link" href="/clients/${userId}">Профиль</a></li>
        </#if>
        <li class="nav-item"><a class="nav-link" href="/logout">Выход</a></li>
    </ul>
</nav>

<!-- Container body -->
<div class="container">
    <div style="padding: 20px">
        <!-- Header -->
        <div class="panel">
            <div class="panel-body">
                <h1>${project.title}</h1>
                <p>${project.description}</p>
            </div>
        </div>
        <div class="container">
            <div class="col-sm-4">
                <#if client.id==userId>
                <div>
                    <button type="submit" onclick="location.href = '/projects/update/${project.id}'" class="btn btn-info"/>
                    Редактировать проект</button>
                </div>
                <div>
                    <button type="submit" onclick="location.href = '№'" class="btn btn-info"/>
                    Вывести деньги с проекта</button>
                </div>
                </#if>
            </div>
        </div>
        <!-- Photo and money sum -->
        <div class="panel">
            <div class="panel-body">
                <!-- Photo -->
                <#if project.imagesIds??>
                    <#list project.imagesIds as imageId>
                        <div class="col-sm-8">
                            <img style='max-width: 1024px; max-height: 1024px; width: auto; height: auto; object-fit: contain;'
                                 src="/projects/image/${imageId}" alt="project image">
                        </div>
                    </#list>
                </#if>

                <!-- Money -->
                <div class="col-sm-4">
                    <div>
                        <h2>${project.moneyCollected}</h2>
                        <p>из ${project.moneyGoal}</p>
                        <div class="progress">
                            <div class="progress-bar progress-bar-info progress-bar-striped active"
                                 style="width: ${percent}%; box-shadow: -1px 10px 10px rgba(91, 192, 222, 0.7);"></div>
                        </div>
                        <br>
                        <form role="form" class="form-inline" action="/operation/payment" method="post"
                              enctype="multipart/form-data">
                            <input name="project_id" type="hidden" class="form-control" value="${project.id}"/>
                            <input name="sumPayment" type="text" class="form-control" placeholder="Введите сумму"/>
                            <input type="submit" class="btn btn-success col-12" value="Поддержать проект"/>
                        </form>
                    </div>
                    <!-- Project days -->
                    <div class="row pt-3">
                        <div class="col-md-4"><b>${project.contributorsCount} человек</b>
                            <p>поддержали проект</p></div>
                        <div class="col-md-4"><b>${finishDate} дней</b>
                            <p>осталось</p></div>
                        <div class="col-md-4"><b>${startDate}</b>
                            <p>Дата запуска</p></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Avatar -->
        <!-- https://bbbootstrap.com/snippets/bootstrap-sidebar-user-profile-62301382 -->
        <div class="container">
            <div class="col-sm-4">
                <div>
                    <div>
                        <div class="d-flex flex-row">
                            <#if client.image??>
                            <div class="image"><img
                                        src="/clients/image/${client.image.id}" width="100" height="100"></div>
                            <div class="ml-3 w-100">
                                </#if>
                                <h5 class="mb-0 mt-0">${client.firstName}</h5>
                                <h5 class="mb-0 mt-0">${client.lastName}</h5>
                                <div class="d-flex justify-content-between rounded stats">
                                    <div class="d-flex flex-column"><span class="articles"> Проектов: ${size}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>