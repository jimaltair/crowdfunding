<#import "spring.ftl" as spring/>
<html>
<head>
    <title>${project.title}</title>
</head>
<body>
<h2>${project.title}</h2>
<#if project.imagesIds??>
    <#list project.imagesIds as imageId>
        <img src="/projects/image/${imageId}" alt="project image"
             style="max-width: 512px; max-height: 512px; width: auto; height: auto;" />
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
