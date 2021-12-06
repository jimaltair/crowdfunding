<#import "spring.ftl" as spring/>
<html>
<head>
    <title>${project.title}</title>
</head>
<body>
<h2>${project.title}</h2>
<text>Описание: ${project.description}</text>
<br>
<text>Автор: ${project.authorName}</text>
<br>
<br>
<text>Создан: ${project.createdAt}</text>
<br>
<text>Дата окончания: ${project.finishDate}</text>
<br>
<br>
<text>Цель: ${project.moneyGoal}</text>
<br>
<text>Собрано: ${project.moneyCollected}</text>
<br>
<text>Участников: ${project.contributorsCount}</text>
<br>
</body>
</html>
