<#import "spring.ftl" as spring/>
<html>
<head>
    <title>${project.title}</title>
</head>
<body>
<h2>${project.title}</h2>
<#if project.imagesLinks??>
    <#list project.imagesLinks as link>
        <img src="${link}" alt="project image" />
        <br>
    </#list>
    <br>
</#if>
<text>Описание: ${project.description}</text>
<br>
<br>
<text>Автор: ${project.authorName}</text>
<br>
<text>Создан: ${project.createdAt}</text>
<br>
<br>
<text>Дата окончания: ${project.finishDate}</text>
<br>
<text>Цель: ${project.moneyGoal}</text>
<br>
<text>Собрано: ${project.moneyCollected}</text>
<br>
<text>Участников: ${project.contributorsCount}</text>
<br>
</body>
</html>
